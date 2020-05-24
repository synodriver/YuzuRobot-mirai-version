package cn.yumetsuki.murasame.repo.dao

import cn.yumetsuki.ktorm.dsl.regexp
import cn.yumetsuki.murasame.repo.entity.RegexCorpus
import cn.yumetsuki.murasame.repo.entity.RegexCorpuses
import kotlinx.coroutines.*
import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.filter
import me.liuwj.ktorm.entity.sequenceOf
import me.liuwj.ktorm.entity.toList

interface RegexCorpusDao {

    suspend fun addRegexCorpus(regexCorpus: RegexCorpus)

    suspend fun findAllRegexCorpus(): List<RegexCorpus>

    suspend fun findRegexCorpusMatchRegex(matchString: String): List<RegexCorpus>

}

class RegexCorpusDaoImpl(
    private val database: Database
): RegexCorpusDao {
    override suspend fun addRegexCorpus(regexCorpus: RegexCorpus) = withContext(Dispatchers.IO) {
        database.sequenceOf(RegexCorpuses).add(regexCorpus)
        Unit
    }

    override suspend fun findAllRegexCorpus(): List<RegexCorpus> = withContext(Dispatchers.IO) {
        database.sequenceOf(RegexCorpuses).toList()
    }

    override suspend fun findRegexCorpusMatchRegex(matchString: String): List<RegexCorpus> = withContext(Dispatchers.IO) {
        database.sequenceOf(RegexCorpuses).filter {
            matchString regexp it.regex
        }.toList()
    }

}