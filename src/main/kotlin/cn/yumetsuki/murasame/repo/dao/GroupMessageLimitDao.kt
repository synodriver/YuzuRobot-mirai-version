package cn.yumetsuki.murasame.repo.dao

import cn.yumetsuki.murasame.repo.entity.GroupMessageLimit
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.time.LocalDateTime

interface GroupMessageLimitDao {

    suspend fun addOrUpdatePersonalMessageLimitAsync(
        groupMessageLimit: GroupMessageLimit
    ): Deferred<Unit>

    suspend fun findLimitByGroupIdAsync(
        groupId: Long
    ): Deferred<GroupMessageLimit>

}

class GroupMessageLimitDaoImpl: GroupMessageLimitDao {

    private val mutex = Mutex()

    //key: QQ号、群号; 默认群内1小时500次上限;
    private val personalLimit: HashMap<Long, Pair<LocalDateTime, Int>> = hashMapOf()

    override suspend fun addOrUpdatePersonalMessageLimitAsync(
        groupMessageLimit: GroupMessageLimit
    ): Deferred<Unit> = GlobalScope.async {
        mutex.withLock {
            personalLimit[groupMessageLimit.groupId] = groupMessageLimit.run {
                firstSendTime to messageCount
            }
            Unit
        }
    }

    override suspend fun findLimitByGroupIdAsync(
        groupId: Long
    ): Deferred<GroupMessageLimit> = GlobalScope.async {

        mutex.withLock {
            personalLimit[groupId]?.let {
                GroupMessageLimit(groupId, it.first, it.second)
            }?:GroupMessageLimit(groupId, LocalDateTime.now(), 0)
        }

    }


}