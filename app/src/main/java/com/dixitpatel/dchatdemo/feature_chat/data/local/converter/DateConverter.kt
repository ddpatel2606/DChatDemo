package com.dixitpatel.dchatdemo.feature_chat.data.local.converter

import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * [TypeConverter] for Room to convert between [LocalDateTime] and [String].
 * This allows storing date and time information in the database as a standardized
 * string format (ISO_LOCAL_DATE_TIME) and retrieving it as a `LocalDateTime` object.
 */
class DateConverter {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    @TypeConverter
    fun fromLocalDateTime(dateTime: LocalDateTime?): String? {
        return dateTime?.format(formatter)
    }

    @TypeConverter
    fun toLocalDateTime(dateTimeString: String?): LocalDateTime? {
        return dateTimeString?.let {
            LocalDateTime.parse(it, formatter)
        }
    }
}