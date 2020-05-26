package cn.yumetsuki.murasame.repo.entity

import java.time.LocalDateTime

enum class GoHanType {
    Breakfast, Lunch, Dinner
}

class GoHanRecord(
        val groupId: Long,
        val userId: Long,
        val recordTime: Long,
        val goHanType: GoHanType
)