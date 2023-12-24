package ru.smak.qrmaps.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
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

object LocationDatabase{

    private lateinit var tracks: TrackDao
    private lateinit var paths: PointsDao

    fun getTracks(applicationContext: Context): TrackDao {
        if (!::tracks.isInitialized)
            tracks = Room.databaseBuilder(
                applicationContext,
                ALocatorDatabase::class.java,
                ALocatorDatabase.DB_NAME,
            ).build().getTrackDao()
        return tracks
    }

    fun getPaths(applicationContext: Context): PointsDao {
        if (!::paths.isInitialized)
            paths = Room.databaseBuilder(
                applicationContext,
                ALocatorDatabase::class.java,
                ALocatorDatabase.DB_NAME,
            ).build().getPathDao()
        return paths
    }
}