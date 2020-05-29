package cn.yumetsuki.murasame.feature.common

import cn.yumetsuki.mirai.data.withLine
import cn.yumetsuki.murasame.feature.other.recordReplyEvent
import cn.yumetsuki.murasame.listeningfilter.tag
import cn.yumetsuki.murasame.repo.dao.LotRecordDao
import cn.yumetsuki.murasame.repo.entity.LotRecord
import cn.yumetsuki.murasame.repo.entity.LotType
import cn.yumetsuki.resource.*
import cn.yumetsuki.util.*
import net.mamoe.mirai.event.GroupMessageSubscribersBuilder
import net.mamoe.mirai.message.data.at
import java.io.File
import java.time.LocalDateTime

fun GroupMessageSubscribersBuilder.lot(intercepted: Boolean = true) {
    val lotRecordDao : LotRecordDao by globalKoin().inject()
    case("@抽签") or (atBot() and content {
        it.contains("抽签")
    }) and tag("lot") reply {
        recordReplyEvent()
        if (intercepted) intercept()
        if (!checkLot(sender.id, group.id, lotRecordDao)) {
            reply(
                    sender.at().withLine(
                            getNetImageStreamFromUrl(ALREADY_LOT_IMG).uploadAsImage() + "汝今天已抽签！"
                    )
            )
        } else {
            val lotType = generateLot()
            lotRecordDao.insertLotRecords(LotRecord.new(sender.id, group.id, LocalDateTime.now().timeStamp(), lotType))
            reply(sender.at().withLine(
                    getNetImageStreamFromUrl(lotType.imageUrl).uploadAsImage() + "既然如此，吾辈就勉为其难地给汝抽一签吧！"
            ))
        }
        Unit
    }
}

private suspend fun checkLot(userId: Long, groupId: Long, lotRecordDao: LotRecordDao): Boolean {
    val today = LocalDateTime.now()
    return lotRecordDao.queryLotRecordBetweenTime(
            userId, groupId, today.nowDayStart().timeStamp() until today.nextDayStart().timeStamp()
    ) == null
}

private suspend fun generateLot() : LotType {
    return LotType.values().random()
}