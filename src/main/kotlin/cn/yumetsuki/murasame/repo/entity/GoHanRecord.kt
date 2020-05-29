package cn.yumetsuki.murasame.repo.entity

import java.time.LocalDateTime

enum class GoHanType(val type: String) {
    Breakfast("早饭"), Lunch("午饭"), Dinner("晚饭")
}

class GoHanRecord(
        val groupId: Long,
        val userId: Long,
        val recordTime: Long,
        val goHanType: GoHanType
)