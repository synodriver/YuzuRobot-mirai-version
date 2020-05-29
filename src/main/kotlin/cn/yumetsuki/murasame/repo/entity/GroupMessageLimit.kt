package cn.yumetsuki.murasame.repo.entity

import java.time.LocalDateTime

data class GroupMessageLimit(
    val groupId: Long,
    val firstSendTime: LocalDateTime,
    val messageCount: Int
)