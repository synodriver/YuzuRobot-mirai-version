package cn.yumetsuki.murasame.repo.entity

import me.liuwj.ktorm.entity.Entity
import me.liuwj.ktorm.schema.Table
import me.liuwj.ktorm.schema.int
import me.liuwj.ktorm.schema.long
import me.liuwj.ktorm.schema.varchar

interface ExecRecord: Entity<ExecRecord> {
    val id: Int
    var userId: Long
    var groupId: Long
    var command: String
    var execResult: String
}

object ExecRecords: Table<ExecRecord>("exec_record") {
    val id by int("id").primaryKey().bindTo { it.id }
    val userId by long("user_id").bindTo { it.userId }
    val groupId by long("group_id").bindTo { it.groupId }
    val command by varchar("command").bindTo { it.command }
    val execResult by varchar("exec_result").bindTo { it.execResult }
}