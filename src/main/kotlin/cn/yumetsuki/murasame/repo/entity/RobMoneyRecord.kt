package cn.yumetsuki.murasame.repo.entity

import me.liuwj.ktorm.entity.Entity
import me.liuwj.ktorm.schema.Table
import me.liuwj.ktorm.schema.int
import me.liuwj.ktorm.schema.long

interface RobMoneyRecord: Entity<RobMoneyRecord> {

    companion object: Entity.Factory<RobMoneyRecord>() {
        fun new(
            userId: Long,
            groupId: Long,
            robMoney: Int,
            recordTime: Long
        ): RobMoneyRecord = RobMoneyRecord {
            this@RobMoneyRecord.userId = userId
            this@RobMoneyRecord.groupId = groupId
            this@RobMoneyRecord.robMoney = robMoney
            this@RobMoneyRecord.recordTime = recordTime
        }
    }

    var userId: Long

    var groupId: Long

    var robMoney: Int

    var recordTime: Long

}

object RobMoneyRecords: Table<RobMoneyRecord>("rob_money_record") {
    val userId by long("user_id").bindTo { it.userId }
    val groupId by long("group_id").bindTo { it.groupId }
    val robMoney by int("rob_money").bindTo { it.robMoney }
    val recordTime by long("record_time").bindTo { it.recordTime }
}