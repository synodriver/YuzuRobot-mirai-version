package cn.yumetsuki.murasame.util

import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

suspend fun setInterval(timeMillis: Long, block: suspend () -> Unit): Job = coroutineScope {
    launch {
        while (true) {
            delay(timeMillis)
            block()
        }
    }
}

suspend fun setTimeout(timeMillis: Long, block: suspend () -> Unit): Job = coroutineScope {
    launch {
        delay(timeMillis)
        block()
    }
}

suspend fun runAtTime(time: LocalTime, block: suspend () -> Unit): Job = coroutineScope {
    launch {
        while (true) {
            val today = LocalDateTime.now()
            val todayInvokeTime = LocalDateTime.of(LocalDate.now(), time)
            val delayMillisSeconds = if (today.isBefore(todayInvokeTime)) {
                Duration.between(today, todayInvokeTime).toMillis()
            } else {
                Duration.between(today, todayInvokeTime.plusDays(1)).toMillis()
            }
            delay(delayMillisSeconds)
            block()
        }
    }
}