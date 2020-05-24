package cn.yumetsuki.murasame.repo.entity

import me.liuwj.ktorm.entity.Entity
import me.liuwj.ktorm.schema.Table
import me.liuwj.ktorm.schema.long
import me.liuwj.ktorm.schema.varchar

enum class LotType(
    val type: String,
    val imageUrl: String,
    val recordUrl: String
) {

    MostLucky("大吉", "https://s2.ax1x.com/2019/10/12/ujkca8.png", "https://yumetsuki-robot.oss-cn-beijing.aliyuncs.com/mur_omkj_1.ogg"),
    MiddleLuckyFirst("中吉", "https://s2.ax1x.com/2019/10/12/ujkrrt.png", "https://yumetsuki-robot.oss-cn-beijing.aliyuncs.com/mur_omkj_3.ogg"),
    MiddleLuckySecond("中吉", "https://s2.ax1x.com/2019/10/12/ujkrrt.png", "https://yumetsuki-robot.oss-cn-beijing.aliyuncs.com/mur_omkj_3.ogg"),
    LuckyFirst("吉","https://s2.ax1x.com/2019/10/12/ujkd8H.png" ,"https://yumetsuki-robot.oss-cn-beijing.aliyuncs.com/mur_omkj_2.ogg"),
    LuckySecond("吉","https://s2.ax1x.com/2019/10/12/ujkd8H.png" ,"https://yumetsuki-robot.oss-cn-beijing.aliyuncs.com/mur_omkj_2.ogg"),
    SmallLuckyFirst("小吉", "https://s2.ax1x.com/2019/10/12/ujk0xA.png", "https://yumetsuki-robot.oss-cn-beijing.aliyuncs.com/mur_omkj_4.ogg"),
    SmallLuckySecond("小吉", "https://s2.ax1x.com/2019/10/12/ujk0xA.png", "https://yumetsuki-robot.oss-cn-beijing.aliyuncs.com/mur_omkj_4.ogg"),
    LastLuckyFirst("末吉", "https://s2.ax1x.com/2019/10/12/ujk6Vf.png", "https://yumetsuki-robot.oss-cn-beijing.aliyuncs.com/mur_omkj_5.ogg"),
    LastLuckySecond("末吉", "https://s2.ax1x.com/2019/10/12/ujk6Vf.png", "https://yumetsuki-robot.oss-cn-beijing.aliyuncs.com/mur_omkj_5.ogg"),
    OminousFirst("凶", "https://s2.ax1x.com/2019/10/12/ujkoq0.png", "https://yumetsuki-robot.oss-cn-beijing.aliyuncs.com/mur_omkj_6.ogg"),
    OminousSecond("凶", "https://s2.ax1x.com/2019/10/12/ujkoq0.png", "https://yumetsuki-robot.oss-cn-beijing.aliyuncs.com/mur_omkj_6.ogg"),
    MostOminous("大凶", "https://s2.ax1x.com/2020/02/09/1fj3m8.jpg", "https://yumetsuki-robot.oss-cn-beijing.aliyuncs.com/mur_omkj_6.ogg")

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