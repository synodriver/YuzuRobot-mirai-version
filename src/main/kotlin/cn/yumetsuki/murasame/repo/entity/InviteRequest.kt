package cn.yumetsuki.murasame.repo.entity

import cn.yumetsuki.murasame.util.timeStamp
import me.liuwj.ktorm.entity.Entity
import me.liuwj.ktorm.schema.Table
import me.liuwj.ktorm.schema.boolean
import me.liuwj.ktorm.schema.long
import me.liuwj.ktorm.schema.varchar
import java.time.LocalDateTime

interface InviteRequest : Entity<InviteRequest> {

    companion object : Entity.Factory<InviteRequest>() {
        fun new(
            groupId: Long,
            applyUserId: Long,
            applyBotId: Long,
            flag: String,
            applyTime: Long = LocalDateTime.now().timeStamp(),
            agreeTime: Long? = null,
            isAgree: Boolean = false
        ) = InviteRequest {
            this.groupId = groupId
            this.applyUserId = applyUserId
            this.applyBotId = applyBotId
            this.flag = flag
            this.applyTime = applyTime
            this.agreeTime = agreeTime
            this.isAgree = isAgree
        }
    }

    var groupId: Long
    var applyUserId: Long
    var applyBotId: Long
    var flag: String
    var applyTime: Long
    var agreeTime: Long?
    var isAgree: Boolean
}

object InviteRequests : Table<InviteRequest>("invite_request") {
    val groupId by long("group_id").primaryKey().bindTo { it.groupId }
    val applyUserId by long("apply_user_id").bindTo { it.applyUserId }
    val applyBotId by long("apply_bot_id").bindTo { it.applyBotId }
    val flag by varchar("flag").bindTo { it.flag }
    val applyTime by long("apply_time").bindTo { it.applyTime }
    val agreeTime by long("agree_time").bindTo { it.agreeTime }
    val isAgree by boolean("is_agree").bindTo { it.isAgree }
}