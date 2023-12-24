package ru.smak.qrmaps.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PointsDao {
    @Insert(entity = PointInfo::class)
    suspend fun insertLocation(point: PointInfo)

    @Query("SELECT * FROM ${ALocatorDatabase.TRACK_CONTENT_TABLE} WHERE track=:trackId ORDER BY TIME")
    suspend fun getPath(trackId: Long): List<PointInfo>
}