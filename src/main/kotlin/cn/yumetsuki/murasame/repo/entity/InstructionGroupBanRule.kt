package cn.yumetsuki.murasame.repo.entity

import me.liuwj.ktorm.entity.Entity
import me.liuwj.ktorm.schema.Table
import me.liuwj.ktorm.schema.long
import me.liuwj.ktorm.schema.varchar

interface InstructionGroupBanRule : Entity<InstructionGroupBanRule> {
    companion object : Entity.Factory<InstructionGroupBanRule>()
    var instructionTag: String
    var groupId: Long
}

object InstructionGroupBanRules : Table<InstructionGroupBanRule>("instruction_group_ban_rule") {
    val instructionTag by varchar("instruction_tag").bindTo { it.instructionTag }
    val groupId by long("group_id").bindTo { it.groupId }
}