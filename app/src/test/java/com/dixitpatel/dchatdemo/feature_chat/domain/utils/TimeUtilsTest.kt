package com.dixitpatel.dchatdemo.feature_chat.domain.utils

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LocalDateTimeExtensionsTest {

    @Test
    fun `secondsUntil returns correct number of seconds`() {
        val start = LocalDateTime.of(2025, 11, 7, 12, 0, 0)
        val end = LocalDateTime.of(2025, 11, 7, 12, 1, 30) // +90 seconds

        val seconds = start.secondsUntil(end)

        assertThat(seconds).isEqualTo(90)
    }

    @Test
    fun `daysUntil returns correct number of days`() {
        val start = LocalDateTime.of(2025, 11, 5, 10, 0)
        val end = LocalDateTime.of(2025, 11, 7, 10, 0)

        val days = start.hourUntil(end)

        assertThat(days).isEqualTo(48)
    }

    @Test
    fun `formatPrettyTimestamp returns Today for same day`() {
        val now = LocalDateTime.now()
        val formatted = now.formatPrettyTimestamp()
        assertThat(formatted).startsWith("Today")
    }

    @Test
    fun `formatPrettyTimestamp returns Yesterday for previous day`() {
        val yesterday = LocalDateTime.now().minusDays(1)
        val formatted = yesterday.formatPrettyTimestamp()
        assertThat(formatted).startsWith("Yesterday")
    }

    @Test
    fun `formatPrettyTimestamp returns weekday for dates within a week`() {
        val threeDaysAgo = LocalDateTime.now().minusDays(3)
        val formatted = threeDaysAgo.formatPrettyTimestamp()
        val expectedDay = threeDaysAgo.format(DateTimeFormatter.ofPattern("EEEE HH:mm"))
        assertThat(formatted).isEqualTo(expectedDay)
    }

    @Test
    fun `formatPrettyTimestamp returns date for older than a week`() {
        val tenDaysAgo = LocalDateTime.now().minusDays(10)
        val formatted = tenDaysAgo.formatPrettyTimestamp()
        val expected = tenDaysAgo.format(DateTimeFormatter.ofPattern("EEE d MMM"))
        assertThat(formatted).isEqualTo(expected)
    }
}