package cn.yumetsuki.murasame.repo.dao

import cn.yumetsuki.murasame.repo.entity.InstructionGroupBanRule
import cn.yumetsuki.murasame.repo.entity.InstructionGroupBanRules
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.sequenceOf
import me.liuwj.ktorm.entity.toList

interface InstructionGroupBanRuleDao {

    suspend fun addBanRule(vararg rules: InstructionGroupBanRule)

    suspend fun deleteBanRule(vararg rules: InstructionGroupBanRule)

    suspend fun updateBanRule(vararg rules: InstructionGroupBanRule)

    suspend fun queryBanRule(): List<InstructionGroupBanRule>

}

class InstructionGroupBanRuleDaoImpl(
    private val database: Database
): InstructionGroupBanRuleDao {
    override suspend fun addBanRule(vararg rules: InstructionGroupBanRule) = withContext(Dispatchers.IO) {
        database.sequenceOf(InstructionGroupBanRules).let { entitySequence ->
            rules.map {
                async { entitySequence.add(it) }
            }.awaitAll()
        }
        Unit
    }

    override suspend fun deleteBanRule(vararg rules: InstructionGroupBanRule) = withContext(Dispatchers.IO) {
        rules.map {
            async { it.delete() }
        }
        Unit
    }

    override suspend fun updateBanRule(vararg rules: InstructionGroupBanRule) = withContext(Dispatchers.IO) {
        rules.map {
            async { it.flushChanges() }
        }.awaitAll()
        Unit
    }

    override suspend fun queryBanRule(): List<InstructionGroupBanRule> = withContext(Dispatchers.IO) {
        database.sequenceOf(InstructionGroupBanRules).toList()
    }


}