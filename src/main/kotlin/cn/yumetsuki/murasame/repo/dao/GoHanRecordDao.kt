package cn.yumetsuki.murasame.repo.dao

import cn.yumetsuki.murasame.repo.entity.GoHanRecord
import cn.yumetsuki.murasame.repo.entity.GoHanType
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

interface GoHanRecordDao {
    suspend fun insertOrUpdateGoHanRecord(record: GoHanRecord)
    suspend fun queryGoHanRecordBetweenTime(groupId: Long, userId: Long, goHanType: GoHanType, range: LongRange) : GoHanRecord?
}

class GoHanRecordDaoImpl : GoHanRecordDao {

    private val records : HashMap<String, GoHanRecord> = hashMapOf()

    private val mutex = Mutex()

    override suspend fun insertOrUpdateGoHanRecord(record: GoHanRecord) {
        mutex.withLock {
            records[record.key] = record
        }
    }

    override suspend fun queryGoHanRecordBetweenTime(groupId: Long, userId: Long, goHanType: GoHanType, range: LongRange) : GoHanRecord? {
        return mutex.withLock {
            records[generateKey(groupId, userId, goHanType)]?.takeIf {
                it.recordTime in range
            }
        }
    }

    private val GoHanRecord.key: String
        get() = generateKey(groupId, userId, goHanType)

    private fun generateKey(groupId: Long, userId: Long, goHanType: GoHanType): String {
        return "${goHanType.name}${groupId}-${userId}"
    }
}