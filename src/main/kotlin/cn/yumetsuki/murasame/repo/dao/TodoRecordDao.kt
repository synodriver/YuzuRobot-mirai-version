package cn.yumetsuki.murasame.repo.dao

import cn.yumetsuki.murasame.repo.entity.TodoRecord
import cn.yumetsuki.murasame.repo.entity.TodoRecords
import kotlinx.coroutines.*
import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.and
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.dsl.greater
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.filter
import me.liuwj.ktorm.entity.sequenceOf
import me.liuwj.ktorm.entity.toList

interface TodoRecordDao {

    suspend fun addTodoRecord(todoRecord: TodoRecord)

    suspend fun findTodoRecordByBothIdBeforeEndTime(
        userId: Long, groupId: Long, time: Long
    ): List<TodoRecord>

}

class TodoRecordDaoImpl(

    private val database: Database
): TodoRecordDao {
    override suspend fun addTodoRecord(todoRecord: TodoRecord) = withContext(Dispatchers.IO) {
        database.sequenceOf(TodoRecords).add(todoRecord)
        Unit
    }

    override suspend fun findTodoRecordByBothIdBeforeEndTime(
        userId: Long,
        groupId: Long,
        time: Long
    ) = withContext(Dispatchers.IO) {

        database.sequenceOf(TodoRecords).filter {
            (it.userId eq userId) and (it.groupId eq groupId) and (it.endTime greater time)
        }.toList()

    }

}