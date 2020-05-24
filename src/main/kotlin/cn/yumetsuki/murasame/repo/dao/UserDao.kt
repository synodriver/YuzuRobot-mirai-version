package cn.yumetsuki.murasame.repo.dao

import cn.yumetsuki.murasame.repo.entity.User
import cn.yumetsuki.murasame.repo.entity.Users
import kotlinx.coroutines.*
import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.and
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.entity.find
import me.liuwj.ktorm.entity.sequenceOf
import me.liuwj.ktorm.entity.toList

interface UserDao {

    suspend fun queryUserByUsernameAndPassword(username: String, password: String): User?

    suspend fun queryUsers(): List<User>

}

class UserDaoImpl(
    private val database: Database
): UserDao {
    override suspend fun queryUserByUsernameAndPassword(username: String, password: String): User? = withContext(Dispatchers.IO) {
        database.sequenceOf(Users).find {
            it.username eq username and (it.password eq password)
        }
    }

    override suspend fun queryUsers(): List<User> = withContext(Dispatchers.IO) {
        database.sequenceOf(Users).toList()
    }
}