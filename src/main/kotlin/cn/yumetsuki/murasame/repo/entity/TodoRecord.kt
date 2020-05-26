package cn.yumetsuki.murasame.repo.entity

import cn.yumetsuki.util.timeStamp
import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.and
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.dsl.greater
import me.liuwj.ktorm.entity.Entity
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.any
import me.liuwj.ktorm.entity.sequenceOf
import me.liuwj.ktorm.schema.Table
import me.liuwj.ktorm.schema.long
import me.liuwj.ktorm.schema.varchar
import java.time.LocalDateTime



enum class TodoType(
    val type: String
) {
    Work("打工"), Exercise("锻炼")
}

interface TodoRecord: Entity<TodoRecord> {

    companion object: Entity.Factory<TodoRecord>() {
        fun new(
            userId: Long,
            groupId: Long,
            endTime: Long,
            todoType: TodoType
        ): TodoRecord = TodoRecord {
            this@TodoRecord.userId = userId
            this@TodoRecord.groupId = groupId
            this@TodoRecord.endTime = endTime
            this@TodoRecord.todoType = todoType.type
        }
    }

    var userId: Long

    var groupId: Long

    var endTime: Long

    var todoType: String

}

object TodoRecords: Table<TodoRecord>("todo_record") {

    val userId by long("user_id").bindTo { it.userId }
    val groupId by long("group_id").bindTo { it.groupId }
    val endTime by long("end_time").bindTo { it.endTime }
    val todoType by varchar("todo_type").bindTo { it.todoType }

}