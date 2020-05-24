package cn.yumetsuki.murasame.repo.entity

import cn.yumetsuki.util.timeStamp
import me.liuwj.ktorm.entity.Entity
import me.liuwj.ktorm.schema.Table
import me.liuwj.ktorm.schema.long
import me.liuwj.ktorm.schema.varchar
import java.time.LocalDateTime

interface RudeSpeak : Entity<RudeSpeak> {

    companion object : Entity.Factory<RudeSpeak>() {
        fun new(
            userId: Long,
            groupId: Long,
            message: String,
            nickName: String? = null,
            groupNickName: String? = null,
            recordTime: Long = LocalDateTime.now().timeStamp()
        ) = RudeSpeak {
            this.userId = userId
            this.groupId = groupId
            this.message = message
            this.nickName = nickName
            this.groupNickName = groupNickName
            this.recordTime = recordTime
        }
    }

    var userId: Long
    var groupId: Long
    var message: String
    var nickName: String?
    var groupNickName: String?
    var recordTime: Long
}

object RudeSpeaks : Table<RudeSpeak>("rude_speak") {
    val userId by long("user_id").bindTo { it.userId }
    val groupId by long("group_id").bindTo { it.groupId }
    val message by varchar("message").bindTo { it.message }
    val nickname by varchar("nickname").bindTo { it.nickName }
    val groupNickName by varchar("group_nick_name").bindTo { it.groupNickName }
    val recordTime by long("record_time").bindTo { it.recordTime }
}