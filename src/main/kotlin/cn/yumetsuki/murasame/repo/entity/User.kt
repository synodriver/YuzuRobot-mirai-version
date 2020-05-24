package cn.yumetsuki.murasame.repo.entity

import me.liuwj.ktorm.entity.Entity
import me.liuwj.ktorm.schema.Table
import me.liuwj.ktorm.schema.long
import me.liuwj.ktorm.schema.varchar

interface User : Entity<User> {

    companion object : Entity.Factory<User>() {
        fun new(
            username: String,
            password: String,
            type: String,
            qq: Long? = null
        ) = User {
            this.username = username
            this.password = password
            this.type = type
            this.qq = qq
        }
    }

    var username: String
    var password: String
    var type: String
    var qq: Long?
}

object Users : Table<User>("user") {
    val username by varchar("username").bindTo { it.username }
    val password by varchar("password").bindTo { it.password }
    val type by varchar("type").bindTo { it.type }
    val qq by long("qq").bindTo { it.qq }
}