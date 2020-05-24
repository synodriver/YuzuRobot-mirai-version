package cn.yumetsuki.murasame.repo.dao

import cn.yumetsuki.murasame.repo.entity.ExecRecord
import cn.yumetsuki.murasame.repo.entity.ExecRecords
import kotlinx.coroutines.*
import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.filter
import me.liuwj.ktorm.entity.sequenceOf
import me.liuwj.ktorm.entity.toList

interface ExecRecordDao {

    suspend fun addExecRecords(vararg execRecords: ExecRecord)

    suspend fun deleteExecRecords(vararg execRecords: ExecRecord)

    suspend fun queryExecRecords(): List<ExecRecord>

    suspend fun queryExecRecordsByGroupId(groupId: Long): List<ExecRecord>

}

class ExecRecordDaoImpl(
    private val database: Database
): ExecRecordDao {
    override suspend fun addExecRecords(vararg execRecords: ExecRecord) = withContext(Dispatchers.IO) {
        database.sequenceOf(ExecRecords).let { entitySequence ->
            execRecords.map {
                async { entitySequence.add(it) }
            }.awaitAll()
        }
        Unit
    }

    override suspend fun deleteExecRecords(vararg execRecords: ExecRecord) = withContext(Dispatchers.IO) {
        execRecords.map {
            async { it.delete() }
        }
        Unit
    }

    override suspend fun queryExecRecords(): List<ExecRecord> = withContext(Dispatchers.IO) {
        database.sequenceOf(ExecRecords).toList()
    }

    override suspend fun queryExecRecordsByGroupId(groupId: Long): List<ExecRecord> = withContext(Dispatchers.IO) {
        database.sequenceOf(ExecRecords).filter {
            it.groupId eq groupId
        }.toList()
    }

}