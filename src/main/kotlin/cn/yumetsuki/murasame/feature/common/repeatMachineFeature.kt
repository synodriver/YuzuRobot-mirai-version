package cn.yumetsuki.murasame.feature.common

import cn.yumetsuki.murasame.feature.other.recordReplyEvent
import kotlinx.coroutines.sync.withLock
import net.mamoe.mirai.event.GroupMessageSubscribersBuilder
import net.mamoe.mirai.message.data.Message

fun GroupMessageSubscribersBuilder.repeatMachine(repeatMax: Int = 3) {

    class RepeatRecord(
        val groupId: Long,
        var message: Message
    ) {
        var count = 1
    }

    val repeatRecords = hashMapOf<Long, RepeatRecord>()

    val mutex = kotlinx.coroutines.sync.Mutex()

    always {
        val isRepeat = mutex.withLock {
            repeatRecords[group.id]?.let {
                if (message.contentEquals(it.message)) {
                    ++it.count
                } else {
                    null
                }
            }?.let {
                if (it == repeatMax) {
                    repeatRecords.remove(group.id)
                    true
                } else false
            }?:run {
                repeatRecords[group.id] = RepeatRecord(group.id, message)
                false
            }
        } && (1..10).random() <= 5
        if (isRepeat) reply(message)
    }

}