package cn.yumetsuki.murasame.repo.dao

import cn.yumetsuki.murasame.repo.entity.Instruction
import cn.yumetsuki.murasame.repo.entity.Instructions
import kotlinx.coroutines.*
import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.find
import me.liuwj.ktorm.entity.sequenceOf
import me.liuwj.ktorm.entity.toList

interface InstructionDao {

    suspend fun addInstructions(vararg instructions: Instruction)

    suspend fun deleteInstructions(vararg instructions: Instruction)

    suspend fun updateInstructions(vararg instructions: Instruction)

    suspend fun queryInstructions(): List<Instruction>

    suspend fun queryInstructionByTag(tag: String): Instruction?

}

class InstructionDaoImpl(
    private val database: Database
): InstructionDao {
    override suspend fun addInstructions(vararg instructions: Instruction) = withContext(Dispatchers.IO) {
        database.sequenceOf(Instructions).let { entitySequence ->
            instructions.map {
                async { entitySequence.add(it) }
            }.awaitAll()
        }
        Unit
    }

    override suspend fun deleteInstructions(vararg instructions: Instruction) = withContext(Dispatchers.IO) {
        instructions.map {
            async { it.delete() }
        }.awaitAll()
        Unit
    }

    override suspend fun updateInstructions(vararg instructions: Instruction) = withContext(Dispatchers.IO) {
        instructions.map {
            async { it.flushChanges() }
        }.awaitAll()
        Unit
    }

    override suspend fun queryInstructions(): List<Instruction> = withContext(Dispatchers.IO) {
        database.sequenceOf(Instructions).toList()
    }

    override suspend fun queryInstructionByTag(tag: String): Instruction? = withContext(Dispatchers.IO) {
        database.sequenceOf(Instructions).find {
            it.tag eq tag
        }
    }

}