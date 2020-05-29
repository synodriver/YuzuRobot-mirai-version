package cn.yumetsuki.murasame.repo.entity

import java.time.LocalDateTime

data class PersonalMessageLimit(
    val userId: Long,
    val groupId: Long,
    val firstSendTime: LocalDateTime,
    val messageCount: Int
)