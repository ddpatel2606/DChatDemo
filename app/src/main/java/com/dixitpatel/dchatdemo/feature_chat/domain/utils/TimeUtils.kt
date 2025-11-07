package com.dixitpatel.dchatdemo.feature_chat.domain.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

/**
 * Extensions for readability
 */
fun LocalDateTime.secondsUntil(other: LocalDateTime) =
    ChronoUnit.SECONDS.between(this, other)

fun LocalDateTime.hourUntil(other: LocalDateTime) =
    ChronoUnit.HOURS.between(this, other)


/**
 * Formatting logic moved into cleaner extensions
 */
fun LocalDateTime.formatPrettyTimestamp(): String {
    val now = LocalDateTime.now()
    val days = ChronoUnit.DAYS.between(this.toLocalDate(), now.toLocalDate())

    return when {
        days == 0L -> "Today ${format(DateTimeFormatter.ofPattern("HH:mm"))}"
        days == 1L -> "Yesterday ${format(DateTimeFormatter.ofPattern("HH:mm"))}"
        days < 7L -> format(DateTimeFormatter.ofPattern("EEEE HH:mm"))
        else -> format(DateTimeFormatter.ofPattern("EEE d MMM"))
    }
}