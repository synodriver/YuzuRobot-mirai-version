package cn.yumetsuki.murasame.repo.dao

import cn.yumetsuki.murasame.repo.entity.LotRecord
import cn.yumetsuki.murasame.repo.entity.LotRecords
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.*
import me.liuwj.ktorm.entity.*

interface LotRecordDao {
    suspend fun queryLotRecordBetweenTime(
            userId: Long,
            groupId: Long,
            range: LongRange
    ) : LotRecord?

    suspend fun queryLotRecords() : List<LotRecord>

    suspend fun queryLotRecordsByUserIdAndGroupId(userId: Long, groupId: Long): List<LotRecord>

    suspend fun insertLotRecords(vararg lotRecords: LotRecord)
}

class LotRecordDaoImpl(
        private val database: Database
) : LotRecordDao {

    override suspend fun queryLotRecords(): List<LotRecord> = withContext(Dispatchers.IO) {
        database.sequenceOf(LotRecords).toList()
    }

    override suspend fun queryLotRecordsByUserIdAndGroupId(userId: Long, groupId: Long): List<LotRecord> = withContext(Dispatchers.IO) {
        database.sequenceOf(LotRecords).filter {
            it.userId eq userId and (it.groupId eq groupId)
        }.toList()
    }

    override suspend fun queryLotRecordBetweenTime(userId: Long, groupId: Long, range: LongRange): LotRecord? = withContext(Dispatchers.IO) {
        database.sequenceOf(LotRecords).find {
            it.userId eq userId and (it.groupId eq groupId) and (it.recordTime greaterEq range.first) and (it.recordTime lessEq range.last)
        }
    }

    override suspend fun insertLotRecords(vararg lotRecords: LotRecord) = withContext(Dispatchers.IO) {
        database.sequenceOf(LotRecords).let { entitySequence ->
            lotRecords.map {
                async { entitySequence.add(it) }
            }.awaitAll()
        }
        Unit
    }

}