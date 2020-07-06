package cn.yumetsuki.murasame.feature.common

import cn.yumetsuki.murasame.feature.other.recordReplyEvent
import cn.yumetsuki.murasame.listeningfilter.tag
import cn.yumetsuki.murasame.repo.dao.QQUserDao
import cn.yumetsuki.murasame.repo.dao.TodoRecordDao
import cn.yumetsuki.murasame.repo.entity.TodoRecord
import cn.yumetsuki.murasame.repo.entity.TodoType
import cn.yumetsuki.util.*
import net.mamoe.mirai.event.GroupMessageSubscribersBuilder
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.PlainText
import java.awt.Font
import java.awt.RenderingHints
import java.io.File
import java.time.LocalDateTime
import java.time.LocalTime
import javax.imageio.ImageIO

fun GroupMessageSubscribersBuilder.todo(intercepted: Boolean = true) {

    val todoRecordDao: TodoRecordDao by globalKoin().inject()
    val qqUserDao: QQUserDao by globalKoin().inject()

    atBot() and contains("打工") quoteReply {
        recordReplyEvent()
        if (intercepted) intercept()
        val msg: Message = message[PlainText]?.let {
            it.content.trim().removePrefix("打工").toIntOrNull()?.let hour@{ hour ->
                val moneyPerHour = 5
                val minHour = 1
                val maxHour = 8
                val workEnableStartTime = LocalTime.of(8, 0, 0)
                val workEnableEndTime = LocalTime.of(21, 0, 0)
                //检查工作时长
                if (hour !in minHour..maxHour) {
                    return@hour ResponseImageBuilder.murasame().apply {
                        add( "kora..这么工作主人会吃不消的啦！...")
                        add("正常一次工作时长: ${minHour}～${maxHour}小时")
                    }.build().uploadImage(group)
                }
                //检查工作开始时间和结束时间
                if (!isBetweenEnableTime(hour, workEnableStartTime, workEnableEndTime)) {
                    return@hour ResponseImageBuilder.murasame().apply {
                        add("唔....这段时间的话...现在芦花姐的田心屋没在营业了呢....")
                        add("田心屋营业时间: ${workEnableStartTime.simpleFormat()}～${workEnableEndTime.simpleFormat()}")
                    }.build().uploadImage(group)
                }
                return@hour todoRecordDao.findTodoRecordByBothIdBeforeEndTime(
                        sender.id, group.id, LocalDateTime.now().timeStamp()
                ).takeIf { todoRecords ->
                    todoRecords.isNotEmpty()
                }?.let { todoRecords ->
                    val lastTodo = todoRecords.maxBy { record -> record.endTime }!!
                    ResponseImageBuilder.murasame().apply {
                        add("主人做事不可以一心二用啦！")
                        add("主人还在${lastTodo.todoType}呢！！")
                    }.build().uploadImage(group)
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
                        ResponseImageBuilder.murasame().apply {
                            add("唔姆，去田心屋打工吗？芦花姐应该会很开心的")
                            add("(获得金币: $increaseMoney, 总金币: ${qqUser.money})")
                        }.build().uploadImage(group)
                    }
                }
            } ?: ResponseImageBuilder.murasame().apply {
                add("唔姆？主人要打工多长时间呢？(示例：打工1)")
            }.build().uploadImage(group)
        } ?: PlainText("唔姆...好像出现了奇怪的错误..")
        quoteReply(msg)
        Unit
    }

    atBot() and contains("锻炼") quoteReply {
        recordReplyEvent()
        if (intercepted) intercept()
        val msg : Message = message[PlainText]?.let {
            it.content.trim().removePrefix("锻炼").toIntOrNull()?.let hour@{ hour ->
                val powerPerHour = 5
                val minHour = 1
                val maxHour = 3
                val exerciseEnableStartTime = LocalTime.of(6, 0, 0)
                val exerciseEnableEndTime = LocalTime.of(22, 0, 0)
                //检查工作时长
                if (hour !in minHour..maxHour) {
                    return@hour ResponseImageBuilder.murasame().apply {
                        add("kora..这么锻炼主人会吃不消的啦！...")
                        add("正常一次锻炼时长: ${minHour}～${maxHour}小时")
                    }.build().uploadImage(group)
                }
                //检查工作开始时间和结束时间
                if (!isBetweenEnableTime(hour, exerciseEnableStartTime, exerciseEnableEndTime)) {
                    return@hour ResponseImageBuilder.murasame().apply {
                        add("唔....这段时间的话...不太适合锻炼吧....")
                        add("可锻炼时间: ${exerciseEnableStartTime.simpleFormat()}～${exerciseEnableEndTime.simpleFormat()}")
                    }.build().uploadImage(group)
                }
                todoRecordDao.findTodoRecordByBothIdBeforeEndTime(
                        sender.id, group.id, LocalDateTime.now().timeStamp()
                ).takeIf { todoRecords ->
                    todoRecords.isNotEmpty()
                }?.let { todoRecords ->
                    val lastTodo = todoRecords.maxBy { record -> record.endTime }!!
                    ResponseImageBuilder.murasame().apply {
                        add("主人做事不可以一心二用啦！")
                        add("主人还在${lastTodo.todoType}呢！！")
                    }.build().uploadImage(group)
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
                        ResponseImageBuilder.murasame().apply {
                            add("主人干巴铁！！！(力量+$increasePower, 总力量: ${qqUser.power}")
                        }.build().uploadImage(group)
                    }
                }
            } ?: ResponseImageBuilder.murasame().apply {
                add("唔姆？主人要锻炼多长时间呢？(示例：锻炼1)")
            }.build().uploadImage(group)
        } ?: PlainText("唔姆...好像出现了奇怪的错误..")
        quoteReply(msg)
        Unit
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