package cn.yumetsuki.murasame.feature.common

import cn.yumetsuki.mirai.data.withLine
import cn.yumetsuki.murasame.feature.other.recordReplyEvent
import cn.yumetsuki.murasame.repo.dao.LotRecordDao
import cn.yumetsuki.murasame.repo.dao.QQUserDao
import cn.yumetsuki.murasame.repo.entity.LotType
import cn.yumetsuki.util.globalKoin
import cn.yumetsuki.util.nextDayStart
import cn.yumetsuki.util.nowDayStart
import cn.yumetsuki.util.timeStamp
import net.mamoe.mirai.event.GroupMessageSubscribersBuilder
import net.mamoe.mirai.message.data.PlainText
import java.time.LocalDateTime

fun GroupMessageSubscribersBuilder.ownInfo(intercepted: Boolean = true) {

    val qqUserDao : QQUserDao by globalKoin().inject()
    val lotRecordDao : LotRecordDao by globalKoin().inject()

    atBot() and content {
        message[PlainText]?.content?.trim() == "资料"
    } quoteReply {
        recordReplyEvent()
        if (intercepted) intercept()
        val user = qqUserDao.findQQUserByUserIdAndGroupIdOrNewDefault(sender.id, group.id)
        val lotRecords = lotRecordDao.queryLotRecordsByUserIdAndGroupId(sender.id, group.id)
        val lotRecord = lotRecordDao.queryLotRecordBetweenTime(
                sender.id, group.id, LocalDateTime.now().nowDayStart().timeStamp()..LocalDateTime.now().nextDayStart().timeStamp()
        )
        PlainText("主人资料: ").withLine(
                "力量: ${user.power} //完善中"
        ).withLine(
                "好感度: ${user.favorite} //完善中"
        ).withLine(
                "资金: ${user.money} //完善中"
        ).withLine(
                "----------"
        ).withLine(
                "群内抽签记录"
        ).withLine(
                "今日: ${lotRecord?.lotType?:"无"}"
        ).withLine("总抽签次数: ${lotRecords.size}").withLine(
                LotType.values().map {
                    it.type
                }.toSet().joinToString("\n") { type ->
                    "$type: ${lotRecords.count { r -> r.lotType == type }}"
                }
        )
    }

}