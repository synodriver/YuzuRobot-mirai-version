package cn.yumetsuki.murasame.repo.entity

import me.liuwj.ktorm.entity.Entity
import me.liuwj.ktorm.schema.Table
import me.liuwj.ktorm.schema.varchar

interface Instruction: Entity<Instruction> {
    var tag: String
    var description: String
}

object Instructions: Table<Instruction>("instruction") {
    val tag by varchar("tag").primaryKey().bindTo { it.tag }
    val description by varchar("description").primaryKey().bindTo { it.description }
}