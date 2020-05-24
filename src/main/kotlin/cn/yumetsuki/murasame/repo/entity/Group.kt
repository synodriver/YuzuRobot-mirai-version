package cn.yumetsuki.murasame.repo.entity

import me.liuwj.ktorm.entity.Entity
import me.liuwj.ktorm.schema.Table
import me.liuwj.ktorm.schema.boolean
import me.liuwj.ktorm.schema.int
import me.liuwj.ktorm.schema.long

interface Group: Entity<Group> {

    companion object : Entity.Factory<Group>() {
        fun new(
            groupId: Long,
            isBlack: Boolean = false,
            limitTime: Int = 3600,
            limitCount: Int = 500,
            personalLimitTime: Int = 60,
            personalLimitCount: Int = 5,
            botUserId: Long = 1,
            enable: Boolean = true
        ) = Group {
            this.groupId = groupId
            this.isBlack = isBlack
            this.limitTime = limitTime
            this.limitCount = limitCount
            this.personalLimitTime = personalLimitTime
            this.personalLimitCount = personalLimitCount
            this.botUserId = botUserId
            this.enable = enable
        }
    }

    var groupId: Long
    var isBlack: Boolean
    var limitTime: Int
    var limitCount: Int
    var personalLimitTime: Int
    var personalLimitCount: Int
    var botUserId: Long
    var enable: Boolean
}

object Groups: Table<Group>("group") {
    val groupId by long("group_id").primaryKey().bindTo { it.groupId }
    val isBlack by boolean("is_black").bindTo { it.isBlack }
    val limitTime by int("limit_time").bindTo { it.limitTime }
    val limitCount by int("limit_count").bindTo { it.limitCount }
    val personalLimitTime by int("personal_limit_time").bindTo { it.personalLimitTime }
    val personalLimitCount by int("personal_limit_count").bindTo { it.personalLimitCount }
    val botUserId by long("bot_user_id").bindTo { it.botUserId }
    val enable by boolean("enable").bindTo { it.enable }
}