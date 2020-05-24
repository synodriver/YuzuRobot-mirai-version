package cn.yumetsuki.murasame.repo.dao

import cn.yumetsuki.murasame.repo.entity.RudeSpeak
import cn.yumetsuki.murasame.repo.entity.RudeSpeaks
import kotlinx.coroutines.*
import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.filter
import me.liuwj.ktorm.entity.sequenceOf
import me.liuwj.ktorm.entity.toList

interface RudeSpeakDao {

    suspend fun addRudeSpeak(vararg rudeSpeaks: RudeSpeak)

    suspend fun queryRudeSpeakByGroupId(groupId: Long): List<RudeSpeak>

    suspend fun queryRudeSpeak(): List<RudeSpeak>

}

class RudeSpeakDaoImpl(
    private val database: Database
): RudeSpeakDao {
    override suspend fun addRudeSpeak(vararg rudeSpeaks: RudeSpeak) = withContext(Dispatchers.IO) {
        database.sequenceOf(RudeSpeaks).let { entitySequence ->
            rudeSpeaks.map {
                async { entitySequence.add(it) }
            }.awaitAll()
        }
        Unit
    }

    override suspend fun queryRudeSpeakByGroupId(groupId: Long): List<RudeSpeak> = withContext(Dispatchers.IO) {
        database.sequenceOf(RudeSpeaks).filter {
            it.groupId eq groupId
        }.toList()
    }

    override suspend fun queryRudeSpeak(): List<RudeSpeak> = withContext(Dispatchers.IO) {
        database.sequenceOf(RudeSpeaks).toList()
    }
}