package ru.smak.qrmaps.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = ALocatorDatabase.TRACK_TABLE)
data class Track(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,

    var owner: Boolean = true,
)