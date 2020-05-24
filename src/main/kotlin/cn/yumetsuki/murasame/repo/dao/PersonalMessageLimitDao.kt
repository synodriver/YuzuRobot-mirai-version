package cn.yumetsuki.murasame.repo.dao

import cn.yumetsuki.murasame.repo.entity.PersonalMessageLimit
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.time.LocalDateTime

interface PersonalMessageLimitDao {

    suspend fun addOrUpdatePersonalMessageLimitAsync(
        personalMessageLimit: PersonalMessageLimit
    ): Deferred<Unit>

    suspend fun findLimitByUserIdAndGroupIdAsync(
        userId: Long, groupId: Long
    ): Deferred<PersonalMessageLimit>

}

class PersonalMessageLimitDaoImpl: PersonalMessageLimitDao {

    private val mutex = Mutex()

    //key: QQ号、群号; 默认个人1分钟5次上限;
    private val personalLimit: HashMap<Pair<Long, Long>, Pair<LocalDateTime, Int>> = hashMapOf()

    override suspend fun addOrUpdatePersonalMessageLimitAsync(
        personalMessageLimit: PersonalMessageLimit
    ): Deferred<Unit> = GlobalScope.async {
        mutex.withLock {
            personalLimit[personalMessageLimit.run { userId to groupId }] = personalMessageLimit.run {
                firstSendTime to messageCount
            }
            Unit
        }
    }

    override suspend fun findLimitByUserIdAndGroupIdAsync(
        userId: Long, groupId: Long
    ): Deferred<PersonalMessageLimit> = GlobalScope.async {

        mutex.withLock {
            personalLimit[userId to groupId]?.let {
                PersonalMessageLimit(userId, groupId, it.first, it.second)
            }?:PersonalMessageLimit(userId, groupId, LocalDateTime.now(), 0)
        }

    }


}