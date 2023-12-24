package ru.smak.qrmaps.database

import android.content.Context
import android.location.Location
import androidx.room.Room
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import java.time.ZoneOffset

object LocationDb{
    private lateinit var tracksDao: TrackDao
    private lateinit var pointsDao: PointsDao

    fun init(applicationContext: Context){
        if (!::tracksDao.isInitialized)
            tracksDao = Room.databaseBuilder(
                applicationContext,
                ALocatorDatabase::class.java,
                ALocatorDatabase.DB_NAME,
            ).build().getTrackDao()
        if (!::pointsDao.isInitialized)
            pointsDao = Room.databaseBuilder(
                applicationContext,
                ALocatorDatabase::class.java,
                ALocatorDatabase.DB_NAME,
            ).build().getPathDao()
    }

    fun getXTracksFlow(): Flow<List<ExtendedTrackInfo>> {
        return tracksDao.getAllTracks().map { list ->
            list.map { track ->
                val path = pointsDao.getPath(track.id)
                var prevLoc = Location("").apply {
                    latitude = path.first().latitude
                    longitude = path.first().longitude
                }
                ExtendedTrackInfo(
                    id = track.id,
                    owner = track.owner,
                    started = path.first().time?.let {LocalDateTime.ofEpochSecond(it / 1000, (it % 1000).toInt(), ZoneOffset.UTC) },
                    finished = path.last().time?.let {LocalDateTime.ofEpochSecond(it / 1000, (it % 1000).toInt(), ZoneOffset.UTC) },
                    length = path.fold(0f){ acc, loc ->
                        var dist: Float
                        prevLoc = Location(prevLoc).apply {
                            latitude = loc.latitude
                            longitude = loc.longitude
                            dist = distanceTo(prevLoc)
                        }
                        acc + dist
                    }
                )
            }
        }
    }

    suspend fun createNewTrack() = tracksDao.insertNewTrack(Track())
    suspend fun addPoint(trackId: Long, location: Location) {
        PointInfo(
            id = 0,
            track = trackId,
            time = location.time,
            latitude = location.latitude,
            longitude = location.longitude,
            speed = location.speed,
            altitude = location.altitude,
            bearing = location.bearing
        ).also {
            pointsDao.insertLocation(it)
        }
    }
}