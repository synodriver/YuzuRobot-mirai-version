package cn.yumetsuki.murasame.repo.entity

import me.liuwj.ktorm.entity.Entity
import me.liuwj.ktorm.schema.Table
import me.liuwj.ktorm.schema.int
import me.liuwj.ktorm.schema.long

interface QQUser: Entity<QQUser> {

    companion object: Entity.Factory<QQUser>() {
        fun new(
            userId: Long,
            groupId: Long,
            power: Int = 0,
            favorite: Int = 0,
            money: Int = 0
        ): QQUser = QQUser {
            this@QQUser.userId = userId
            this@QQUser.groupId = groupId
            this@QQUser.power = power
            this@QQUser.favorite = favorite
            this@QQUser.money = money
        }
    }

    val id: Int
    var userId: Long
    var groupId: Long
    var power: Int
    var favorite: Int
    var money: Int

}

object QQUsers: Table<QQUser>("qq_user") {
    val id by int("id").primaryKey().bindTo { it.id }
    val userId by long("user_id").bindTo { it.userId }
    val groupId by long("group_id").bindTo { it.groupId }
    val power by int("power").bindTo { it.power }
    val favorite by int("favorite").bindTo { it.favorite }
    val money by int("money").bindTo { it.money }
}