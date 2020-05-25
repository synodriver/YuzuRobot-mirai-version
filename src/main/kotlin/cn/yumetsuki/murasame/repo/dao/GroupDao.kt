package cn.yumetsuki.murasame.repo.dao

import cn.yumetsuki.murasame.repo.entity.Group
import cn.yumetsuki.murasame.repo.entity.Groups
import kotlinx.coroutines.*
import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.find
import me.liuwj.ktorm.entity.sequenceOf
import me.liuwj.ktorm.entity.toList

interface GroupDao {

    suspend fun insertGroups(vararg groups: Group)

    suspend fun deleteGroups(vararg groups: Group)

    suspend fun updateGroups(vararg groups: Group)

    suspend fun queryGroups(): List<Group>

    suspend fun queryGroupById(groupId: Long): Group?

}

class GroupDaoImpl(
    private val database: Database
): GroupDao {
    override suspend fun insertGroups(vararg groups: Group) = withContext(Dispatchers.IO) {
        database.sequenceOf(Groups).let { entitySequence ->
            groups.map {
                async { entitySequence.add(it) }
            }.awaitAll()
        }
        Unit
    }

    override suspend fun deleteGroups(vararg groups: Group) = withContext(Dispatchers.IO) {
        groups.map {
            async { it.delete() }
        }.awaitAll()
        Unit
    }

    override suspend fun updateGroups(vararg groups: Group) = withContext(Dispatchers.IO) {
        groups.map {
            async { it.flushChanges() }
        }.awaitAll()
        Unit
    }

    override suspend fun queryGroups(): List<Group> = withContext(Dispatchers.IO) {
        database.sequenceOf(Groups).toList()
    }

    override suspend fun queryGroupById(groupId: Long): Group? = withContext(Dispatchers.IO) {
        database.sequenceOf(Groups).find {
            it.groupId eq groupId
        }
    }
}