package cn.yumetsuki.murasame.repo.entity

import me.liuwj.ktorm.entity.Entity
import me.liuwj.ktorm.schema.Table
import me.liuwj.ktorm.schema.long

interface BlackUser : Entity<BlackUser> {
    companion object : Entity.Factory<BlackUser>()
    var userId: Long
}

object BlackUsers : Table<BlackUser>("black_user") {
    val userId by long("user_id").primaryKey().bindTo { it.userId }
}