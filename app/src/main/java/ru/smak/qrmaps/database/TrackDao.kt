package ru.smak.qrmaps.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackDao {
    @Insert(entity = Track::class)
    suspend fun insertNewTrack(track: Track)

    @Update(entity = Track::class)
    suspend fun updateTrack(track: Track)

    @Delete(entity = Track::class)
    suspend fun deleteTrack(track: Track)

    @Query("select * from ${ALocatorDatabase.TRACK_TABLE} where id=:id")
    fun getTrack(id: Long): Track

    @Query("select * from ${ALocatorDatabase.TRACK_TABLE}")
    fun getAllTracks(id: Long): Flow<List<Track>>
}