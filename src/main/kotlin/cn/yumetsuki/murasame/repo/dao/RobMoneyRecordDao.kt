package cn.yumetsuki.murasame.repo.dao

import cn.yumetsuki.murasame.repo.entity.RobMoneyRecord
import cn.yumetsuki.murasame.repo.entity.RobMoneyRecords
import cn.yumetsuki.util.fromTimeStamp
import kotlinx.coroutines.*
import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.and
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.filter
import me.liuwj.ktorm.entity.sequenceOf
import java.time.LocalDate

interface RobMoneyRecordDao {

    suspend fun findRobMoneyRecordsByUserIdAndGroupIdAndDate(
        userId: Long,
        groupId: Long,
        date: LocalDate
    ): List<RobMoneyRecord>

    suspend fun addRobMoneyRecord(robMoneyRecord: RobMoneyRecord)

}

class RobMoneyRecordDaoImpl(
    private val database: Database
): RobMoneyRecordDao {

    override suspend fun findRobMoneyRecordsByUserIdAndGroupIdAndDate(
        userId: Long,
        groupId: Long,
        date: LocalDate
    ): List<RobMoneyRecord> = withContext(Dispatchers.IO) {

        database.sequenceOf(RobMoneyRecords)
            .filter {
                (it.userId eq userId) and (it.groupId eq groupId)
            }.asKotlinSequence().filter {
                it.recordTime.fromTimeStamp().toLocalDate().isEqual(date)
            }.toList()

    }

    override suspend fun addRobMoneyRecord(robMoneyRecord: RobMoneyRecord) = withContext(Dispatchers.IO) {
        database.sequenceOf(RobMoneyRecords).add(robMoneyRecord)
        Unit
    }

}