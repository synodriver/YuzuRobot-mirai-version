package cn.yumetsuki.murasame.repo.entity

import java.time.LocalDateTime

class GroupMessageLimit(
    val groupId: Long,
    val firstSendTime: LocalDateTime,
    val messageCount: Int
)