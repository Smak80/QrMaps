package ru.smak.qrmaps.database

import android.location.Location
import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

class LocalDateTimeConverter {
    @TypeConverter
    fun epochSecondToLocalDateTime(value: Long?): LocalDateTime? = value?.let{
        LocalDateTime.ofEpochSecond(
            it,
            0,
            ZoneOffset.UTC
        )
    }

    @TypeConverter
    fun localDateTimeToEpochDay(date: LocalDateTime?): Long? =
        date?.toEpochSecond(ZoneOffset.UTC)
}

