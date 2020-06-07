package cn.yumetsuki.murasame.feature.common

import cn.yumetsuki.murasame.listeningfilter.tag
import cn.yumetsuki.util.timeStamp
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import net.mamoe.mirai.contact.nameCardOrNick
import net.mamoe.mirai.event.GroupMessageSubscribersBuilder
import net.mamoe.mirai.event.events.MessageRecallEvent
import net.mamoe.mirai.event.events.operatorOrBot
import net.mamoe.mirai.message.data.*
import java.time.LocalDateTime
import kotlin.collections.LinkedHashMap

data class RecallKey(
        val groupId: Long,
        val messageId: Int
)

private val mutex = Mutex()

class MessageOutTimeHashMap<K>(
        val outSeconds: Long
): LinkedHashMap<K, MessageChain>() {

    override fun put(key: K, value: MessageChain): MessageChain? {
        val result = super.put(key, value)
        entries.firstOrNull()?.let { willRemoved ->
            if (LocalDateTime.now().timeStamp() / 1000 - willRemoved.value.time > outSeconds) {
                remove(willRemoved.key)
            }
        }
        return result
    }

}

private val recalls = MessageOutTimeHashMap<RecallKey>(2 * 60)

fun GroupMessageSubscribersBuilder.recordMessage() {
    tag("preventRecall") reply {
        mutex.withLock {
            recalls[RecallKey(group.id, message.id)] = message
        }
        Unit
    }
}

suspend fun MessageRecallEvent.GroupRecall.replyRecallMessage() {
    tag("preventRecall") {
        mutex.withLock {
            val key = RecallKey(group.id, messageId)
            recalls[key]?.takeIf {
                LocalDateTime.now().timeStamp() / 1000 - it.time < recalls.outSeconds
            }?.also {
                recalls.remove(key)
                val reply = PlainText("唔姆...刚刚 ") + operatorOrBot.at() + " 撤回了一条消息！狗修金快来抓住ta！: \n" + it
                this.group.sendMessage(reply)
            }
        }
    }
}