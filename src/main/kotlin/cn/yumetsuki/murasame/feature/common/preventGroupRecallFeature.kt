package cn.yumetsuki.murasame.feature.common

import kotlinx.coroutines.sync.Mutex
import net.mamoe.mirai.event.events.MessageRecallEvent
import net.mamoe.mirai.message.data.Message

data class RecallKey(
        val groupId: Long,
        val messageId: Long
)

private val mutex = Mutex()

private val recalls = hashMapOf<RecallKey, Message>()

suspend fun MessageRecallEvent.GroupRecall.replyRecallMessage() {

}