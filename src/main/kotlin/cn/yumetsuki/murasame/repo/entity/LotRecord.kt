package cn.yumetsuki.murasame.repo.entity

import cn.yumetsuki.resource.*
import me.liuwj.ktorm.entity.Entity
import me.liuwj.ktorm.schema.Table
import me.liuwj.ktorm.schema.long
import me.liuwj.ktorm.schema.varchar

enum class LotType(
        val type: String,
        val imageUrl: String
) {

    MostLuckyFirst("大吉", LOT_MOST_LUCKY_IMG),
    MostLuckySecond("大吉", LOT_MOST_LUCKY_IMG),
    MiddleLuckyFirst("中吉", LOT_MIDDLE_LUCKY_IMG),
    MiddleLuckySecond("中吉", LOT_MIDDLE_LUCKY_IMG),
    MiddleLuckyThird("中吉", LOT_MIDDLE_LUCKY_IMG),
    LuckyFirst("吉", LOT_LUCKY_IMG),
    LuckySecond("吉", LOT_LUCKY_IMG),
    LuckyThird("吉", LOT_LUCKY_IMG),
    SmallLuckyFirst("小吉", LOT_SMALL_LUCKY_IMG),
    SmallLuckySecond("小吉", LOT_SMALL_LUCKY_IMG),
    SmallLuckyThird("小吉", LOT_SMALL_LUCKY_IMG),
    LastLuckyFirst("末吉", LOT_LAST_LUCKY_IMG),
    LastLuckySecond("末吉", LOT_LAST_LUCKY_IMG),
    LastLuckyThird("末吉", LOT_LAST_LUCKY_IMG),
    OminousFirst("凶", LOT_OMINOUS_IMG),
    OminousSecond("凶", LOT_OMINOUS_IMG),
    OminousThird("凶", LOT_OMINOUS_IMG),
    MostOminous("大凶", LOT_MOST_OMINOUS_IMG)

}

interface LotRecord: Entity<LotRecord> {

    companion object: Entity.Factory<LotRecord>() {
        fun new(
            userId: Long,
            groupId: Long,
            recordTime: Long,
            lotType: LotType
        ): LotRecord = LotRecord {
            this@LotRecord.userId = userId
            this@LotRecord.groupId = groupId
            this@LotRecord.recordTime = recordTime
            this@LotRecord.lotType = lotType.type
        }
    }

    var userId: Long

    var groupId: Long

    var recordTime: Long

    var lotType: String

}

object LotRecords: Table<LotRecord>("lot_record") {

    val userId by long("user_id").bindTo { it.userId }
    val groupId by long("group_id").bindTo { it.groupId }
    val recordTime by long("record_time").bindTo { it.recordTime }
    val lotType by varchar("lot_type").bindTo { it.lotType }

}