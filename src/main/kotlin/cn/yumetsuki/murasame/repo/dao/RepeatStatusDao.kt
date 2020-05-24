package cn.yumetsuki.murasame.repo.dao

import cn.yumetsuki.murasame.repo.entity.RepeatStatus
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

interface RepeatStatusDao {

    fun addOrUpdateRepeatStatusAsync(repeatStatus: RepeatStatus): Deferred<Unit>

    fun findRepeatStatusByGroupIdAsync(groupId: Long): Deferred<RepeatStatus>

}

class RepeatStatusDaoImpl: RepeatStatusDao {

    private val mutex = Mutex()

    //key: groupId -> value: message to repeatCount
    private val repeatStatusMap: HashMap<Long, Pair<String, Int>> = hashMapOf()

    override fun addOrUpdateRepeatStatusAsync(
        repeatStatus: RepeatStatus
    ): Deferred<Unit> = GlobalScope.async {
        mutex.withLock {
            repeatStatusMap[repeatStatus.groupId] = repeatStatus.run { message to count }
            Unit
        }
    }
    override fun findRepeatStatusByGroupIdAsync(
        groupId: Long
    ): Deferred<RepeatStatus> = GlobalScope.async {
        mutex.withLock {
            repeatStatusMap[groupId]?.let {
                RepeatStatus(
                    groupId,
                    it.first,
                    it.second
                )
            }?:RepeatStatus(groupId, "", 0)
        }
    }


}