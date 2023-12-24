package ru.smak.qrmaps.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Track::class, PointInfo::class], version = 1, exportSchema = false)
@TypeConverters(LocalDateTimeConverter::class)
abstract class ALocatorDatabase : RoomDatabase(){
    companion object{
        const val DB_NAME = "db_tracks"
        const val TRACK_CONTENT_TABLE = "coords"
        const val TRACK_TABLE = "tracks"
    }

    abstract fun getTrackDao(): TrackDao
    abstract fun getPathDao(): PointsDao
}