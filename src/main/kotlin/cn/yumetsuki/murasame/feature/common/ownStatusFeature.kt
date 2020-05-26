package cn.yumetsuki.murasame.feature.common

import cn.yumetsuki.mirai.data.withLine
import cn.yumetsuki.murasame.repo.dao.TodoRecordDao
import cn.yumetsuki.util.fromTimeStamp
import cn.yumetsuki.util.globalKoin
import cn.yumetsuki.util.simpleFormat
import cn.yumetsuki.util.timeStamp
import net.mamoe.mirai.event.GroupMessageSubscribersBuilder
import net.mamoe.mirai.message.data.PlainText
import java.time.LocalDateTime

fun GroupMessageSubscribersBuilder.ownStatus() {

    val todoRecordDao : TodoRecordDao by globalKoin().inject()

    atBot() and content {
        message[PlainText]?.content?.trim() == "状态"
    } quoteReply {
        todoRecordDao.findTodoRecordByBothIdBeforeEndTime(
                sender.id, group.id, LocalDateTime.now().timeStamp()
        ).maxBy { it.endTime }?.let {
            PlainText("----------当前状态----------").withLine(
                    "进行中: ${it.todoType}"
            ).withLine(
                    "结束时间: ${it.endTime.fromTimeStamp().simpleFormat()}"
            )
        }?:"当前状态：摸鱼"
    }

}