package cn.yumetsuki.murasame.repo.dao

import cn.yumetsuki.murasame.repo.entity.AtStatus
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

interface AtStatusDao {

    fun addOrUpdateAtStatusAsync(atStatus: AtStatus): Deferred<Unit>

    fun findAtStatusByUserIdAndGroupIdAsync(
        userId: Long,
        groupId: Long
    ): Deferred<AtStatus>

}

class AtStatusDaoImpl: AtStatusDao {

    //key: userId to groupId; value: atCount
    private val atStatusMap: HashMap<Pair<Long, Long>, Int> = hashMapOf()

    private val mutex: Mutex = Mutex()

    override fun addOrUpdateAtStatusAsync(
        atStatus: AtStatus
    ): Deferred<Unit> = GlobalScope.async {
        mutex.withLock {

            val key = atStatus.run { userId to groupId }

            val value = atStatus.atCount

            atStatusMap[key] = value

            Unit
        }
    }

    override fun findAtStatusByUserIdAndGroupIdAsync(
        userId: Long, groupId: Long
    ): Deferred<AtStatus> = GlobalScope.async {
        mutex.withLock {
            val atCount = atStatusMap[userId to groupId]?:0
            AtStatus(userId, groupId, atCount)
        }
    }

}