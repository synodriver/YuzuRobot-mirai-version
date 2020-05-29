package cn.yumetsuki.murasame.feature.common

import cn.yumetsuki.murasame.feature.other.recordReplyEvent
import cn.yumetsuki.murasame.repo.dao.GoHanRecordDao
import cn.yumetsuki.murasame.repo.dao.QQUserDao
import cn.yumetsuki.murasame.repo.entity.GoHanRecord
import cn.yumetsuki.murasame.repo.entity.GoHanType
import cn.yumetsuki.util.globalKoin
import cn.yumetsuki.util.simpleFormat
import cn.yumetsuki.util.timeStamp
import net.mamoe.mirai.event.GroupMessageSubscribersBuilder
import net.mamoe.mirai.message.GroupMessageEvent
import java.time.LocalDateTime

fun GroupMessageSubscribersBuilder.eatTogether(intercepted: Boolean = true) {

    val goHanRecordDao: GoHanRecordDao by globalKoin().inject()
    val qqUserDao: QQUserDao by globalKoin().inject()

    suspend fun GroupMessageEvent.eatTogetherImpl(
            startTime: LocalDateTime,
            endTime: LocalDateTime,
            favoriteAdd: Int,
            goHanType: GoHanType
    ): String {
        recordReplyEvent()
        if (intercepted) intercept()
        val now = LocalDateTime.now()
        return if (now.isAfter(startTime) && now.isBefore(endTime)) {
            goHanRecordDao.queryGoHanRecordBetweenTime(
                    group.id, sender.id, goHanType, startTime.timeStamp()..endTime.timeStamp()
            )?.let {
                "(唔....刚刚吃过${goHanType.type}...肚子已经饱了"
            } ?: goHanRecordDao.insertOrUpdateGoHanRecord(
                    GoHanRecord(group.id, sender.id, now.timeStamp(), goHanType)
            ).run {
                qqUserDao.findQQUserByUserIdAndGroupIdOrNewDefault(
                        sender.id, group.id
                ).let { qqUser ->
                    qqUser.favorite += favoriteAdd
                    qqUserDao.updateQQUser(qqUser)
                    "唔姆～今天也和主人一起开心地吃${goHanType.type}了呢～(好感度+$favoriteAdd, 当前好感度: ${qqUser.favorite}"
                }
            }
        } else {
            "(唔....现在已经不是吃${goHanType.type}的时间了呢, ${goHanType.type}时间: ${startTime.toLocalTime().simpleFormat()}～${endTime.toLocalTime().simpleFormat()}"
        }
    }

    atBot() and contains("一起吃早饭") quoteReply {
        val favoriteAdd = 10
        val startTime = LocalDateTime.now().withHour(7).withMinute(0)
                .withSecond(0)
        val endTime = LocalDateTime.now().withHour(8).withMinute(20)
                .withSecond(0)
        quoteReply(eatTogetherImpl(startTime, endTime, favoriteAdd, GoHanType.Breakfast))
        Unit
    }

    atBot() and contains("一起吃午饭") quoteReply {
        val favoriteAdd = 10
        val startTime = LocalDateTime.now().withHour(12).withMinute(0)
                .withSecond(0)
        val endTime = LocalDateTime.now().withHour(13).withMinute(20)
                .withSecond(0)
        quoteReply(eatTogetherImpl(startTime, endTime, favoriteAdd, GoHanType.Lunch))
        Unit
    }

    atBot() and contains("一起吃晚饭") quoteReply {
        val favoriteAdd = 10
        val startTime = LocalDateTime.now().withHour(17).withMinute(30)
                .withSecond(0)
        val endTime = LocalDateTime.now().withHour(19).withMinute(30)
                .withSecond(0)
        quoteReply(eatTogetherImpl(startTime, endTime, favoriteAdd, GoHanType.Dinner))
        Unit
    }

}