package cn.yumetsuki.murasame.repo.dao

import cn.yumetsuki.murasame.repo.entity.QQUser
import cn.yumetsuki.murasame.repo.entity.QQUsers
import kotlinx.coroutines.*
import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.*
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.find
import me.liuwj.ktorm.entity.sequenceOf

interface QQUserDao {

    suspend fun findQQUserByUserIdAndGroupIdOrNewDefault(
        userId: Long,
        groupId: Long
    ): QQUser

    suspend fun updateQQUser(qqUser: QQUser)

}

class QQUserDaoImpl(
    private val database: Database
): QQUserDao {

    override suspend fun findQQUserByUserIdAndGroupIdOrNewDefault(
        userId: Long, groupId: Long
    ): QQUser = withContext(Dispatchers.IO) {

        val sequence = database.sequenceOf(QQUsers)
        sequence.find {
            (it.userId eq userId) and (it.groupId eq groupId)
        }?: QQUser.new(
            userId, groupId
        ).also {
            sequence.add(it)
        }
    }

    override suspend fun updateQQUser(
        qqUser: QQUser
    ) = withContext(Dispatchers.IO) {
        qqUser.flushChanges()
        Unit
    }

}