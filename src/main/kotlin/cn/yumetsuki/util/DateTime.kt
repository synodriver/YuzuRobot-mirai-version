package cn.yumetsuki.util

import java.time.*

fun LocalDateTime.nextDayStart(): LocalDateTime {
    return nowDayStart().plusDays(1)
}

fun LocalDateTime.nowDayStart(): LocalDateTime {
    return this.withHour(0).withMinute(0).withSecond(0)
}

fun LocalDateTime.timeStamp(): Long {
    return toInstant(ZoneOffset.of("+8")).toEpochMilli()
}

fun Long.fromTimeStamp(): LocalDateTime {
    return LocalDateTime.ofInstant(
        Instant.ofEpochMilli(this),
        ZoneId.systemDefault()
    )
}

fun LocalTime.simpleFormat(): String {
    return listOf(
        hour.padZero(), minute.padZero(), second.padZero()
    ).joinToString(":")
}

fun LocalDateTime.simpleFormat(): String {
    return listOf(
        year, monthValue.padZero(), dayOfMonth.padZero()
    ).joinToString("-") + " " + listOf(
        hour.padZero(), minute.padZero(), second.padZero()
    ).joinToString(":")
}

private fun Int.padZero(): String {
    return if (this < 10) return "0$this" else "$this"
}