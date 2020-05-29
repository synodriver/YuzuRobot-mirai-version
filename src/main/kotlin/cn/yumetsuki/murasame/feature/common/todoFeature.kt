package cn.yumetsuki.murasame.feature.common

import cn.yumetsuki.murasame.feature.other.recordReplyEvent
import cn.yumetsuki.murasame.listeningfilter.tag
import cn.yumetsuki.murasame.repo.dao.QQUserDao
import cn.yumetsuki.murasame.repo.dao.TodoRecordDao
import cn.yumetsuki.murasame.repo.entity.TodoRecord
import cn.yumetsuki.murasame.repo.entity.TodoType
import cn.yumetsuki.util.globalKoin
import cn.yumetsuki.util.simpleFormat
import cn.yumetsuki.util.timeStamp
import net.mamoe.mirai.event.GroupMessageSubscribersBuilder
import net.mamoe.mirai.message.data.PlainText
import java.time.LocalDateTime
import java.time.LocalTime

fun GroupMessageSubscribersBuilder.todo(intercepted: Boolean = true) {

    val todoRecordDao: TodoRecordDao by globalKoin().inject()
    val qqUserDao: QQUserDao by globalKoin().inject()

    atBot() and contains("打工") quoteReply {
        recordReplyEvent()
        if (intercepted) intercept()
        message[PlainText]?.let {
            it.content.trim().removePrefix("打工").toIntOrNull()?.let hour@{ hour ->
                val moneyPerHour = 5
                val minHour = 1
                val maxHour = 8
                val workEnableStartTime = LocalTime.of(8, 0, 0)
                val workEnableEndTime = LocalTime.of(21, 0, 0)
                //检查工作时长
                if (hour !in minHour..maxHour) return@hour "kora..这么工作主人会吃不消的啦！...正常一次工作时长: ${minHour}～${maxHour}小时"
                //检查工作开始时间和结束时间
                if (!isBetweenEnableTime(hour, workEnableStartTime, workEnableEndTime)) {
                    return@hour "唔....这段时间的话...现在芦花姐的田心屋没在营业了呢....田心屋营业时间: ${workEnableStartTime.simpleFormat()}～${workEnableEndTime.simpleFormat()}"
                }
                todoRecordDao.findTodoRecordByBothIdBeforeEndTime(
                        sender.id, group.id, LocalDateTime.now().timeStamp()
                ).takeIf { todoRecords ->
                    todoRecords.isNotEmpty()
                }?.let { todoRecords ->
                    val lastTodo = todoRecords.maxBy { record -> record.endTime }!!
                    "主人做事不可以一心二用啦！主人还在${lastTodo.todoType}呢！！"
                }?:todoRecordDao.addTodoRecord(TodoRecord.new(
                        sender.id,
                        group.id,
                        LocalDateTime.now().plusHours(hour.toLong()).timeStamp(),
                        TodoType.Work
                )).run {
                    qqUserDao.findQQUserByUserIdAndGroupIdOrNewDefault(sender.id, group.id).let { qqUser ->
                        val increaseMoney = moneyPerHour * hour
                        qqUser.money += increaseMoney
                        qqUserDao.updateQQUser(qqUser)
                        "唔姆，去田心屋打工吗？芦花姐应该会很开心的(获得金币: $increaseMoney, 总金币: ${qqUser.money}"
                    }
                }
            } ?: "唔姆？主人要打工多长时间呢？(示例：打工1)"
        } ?: "唔姆...好像出现了奇怪的错误.."
    }

    atBot() and contains("锻炼") quoteReply {
        message[PlainText]?.let {
            it.content.trim().removePrefix("锻炼").toIntOrNull()?.let hour@{ hour ->
                val powerPerHour = 5
                val minHour = 1
                val maxHour = 3
                val exerciseEnableStartTime = LocalTime.of(6, 0, 0)
                val exerciseEnableEndTime = LocalTime.of(22, 0, 0)
                //检查工作时长
                if (hour !in minHour..maxHour) return@hour "kora..这么锻炼主人会吃不消的啦！...正常一次锻炼时长: ${minHour}～${maxHour}小时"
                //检查工作开始时间和结束时间
                if (!isBetweenEnableTime(hour, exerciseEnableStartTime, exerciseEnableEndTime)) {
                    return@hour "唔....这段时间的话...不太适合锻炼吧....可锻炼时间: ${exerciseEnableStartTime.simpleFormat()}～${exerciseEnableEndTime.simpleFormat()}"
                }
                todoRecordDao.findTodoRecordByBothIdBeforeEndTime(
                        sender.id, group.id, LocalDateTime.now().timeStamp()
                ).takeIf { todoRecords ->
                    todoRecords.isNotEmpty()
                }?.let { todoRecords ->
                    val lastTodo = todoRecords.maxBy { record -> record.endTime }!!
                    "主人做事不可以一心二用啦！主人还在${lastTodo.todoType}呢！！"
                }?:todoRecordDao.addTodoRecord(TodoRecord.new(
                        sender.id,
                        group.id,
                        LocalDateTime.now().plusHours(hour.toLong()).timeStamp(),
                        TodoType.Work
                )).run {
                    qqUserDao.findQQUserByUserIdAndGroupIdOrNewDefault(sender.id, group.id).let { qqUser ->
                        val increasePower = powerPerHour * hour
                        qqUser.power += increasePower
                        qqUserDao.updateQQUser(qqUser)
                        "主人干巴铁！！！(力量+$increasePower, 总力量: ${qqUser.power}"
                    }
                }
            } ?: "唔姆？主人要锻炼多长时间呢？(示例：锻炼1)"
        } ?: "唔姆...好像出现了奇怪的错误.."
    }

}

private fun isBetweenEnableTime(
        workDuration: Int,
        enableStartTime: LocalTime,
        enableEndTime: LocalTime
): Boolean {
    val now = LocalDateTime.now()
    val startTime = now.withHour(
            enableStartTime.hour
    ).withMinute(
            enableStartTime.minute
    ).withSecond(
            enableStartTime.second
    )
    val endTime = now.withHour(
            enableEndTime.hour
    ).withMinute(
            enableEndTime.minute
    ).withSecond(
            enableEndTime.second
    )
    return now.isAfter(startTime) && now.plusHours(workDuration.toLong()).isBefore(endTime)
}