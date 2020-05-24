package cn.yumetsuki.murasame.repo.dao

import cn.yumetsuki.murasame.repo.entity.BlackUser
import cn.yumetsuki.murasame.repo.entity.BlackUsers
import kotlinx.coroutines.*
import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.sequenceOf
import me.liuwj.ktorm.entity.toList

interface BlackUserDao {

    suspend fun addBlackUser(vararg blackUsers: BlackUser)

    suspend fun deleteBlackUser(vararg blackUsers: BlackUser)

    suspend fun queryBlackUsers(): List<BlackUser>

}

class BlackUserDaoImpl(
    private val database: Database
): BlackUserDao {

    override suspend fun queryBlackUsers(): List<BlackUser> = withContext(Dispatchers.IO) {
        database.sequenceOf(BlackUsers).toList()
    }

    override suspend fun deleteBlackUser(vararg blackUsers: BlackUser) = withContext(Dispatchers.IO) {
        blackUsers.map {
            async { it.delete() }
        }.awaitAll()
        Unit
    }

    override suspend fun addBlackUser(vararg blackUsers: BlackUser) = withContext(Dispatchers.IO) {
        database.sequenceOf(BlackUsers).let { entitySequence ->
            blackUsers.map {
                async { entitySequence.add(it) }
            }.awaitAll()
        }
        Unit
    }


}