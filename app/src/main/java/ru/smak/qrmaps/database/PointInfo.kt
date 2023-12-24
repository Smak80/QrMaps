package ru.smak.qrmaps.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = ALocatorDatabase.TRACK_CONTENT_TABLE,
    foreignKeys = [
        ForeignKey(
            entity = Track::class,
            parentColumns = ["id"],
            childColumns = ["track"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE)
    ]
)
data class PointInfo(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,

    var track: Long,

    var time: Long? = null,

    @ColumnInfo(name="lat")
    var latitude: Double,

    @ColumnInfo(name="lon")
    var longitude: Double,

    @ColumnInfo(name="spd")
    var speed: Float? = null,

    @ColumnInfo(name="alt")
    var altitude: Double? = null,

    @ColumnInfo(name="bea")
    var bearing: Float? = null,
)