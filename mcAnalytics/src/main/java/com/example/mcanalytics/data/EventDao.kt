package com.example.mcanalytics.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(event: AnalyticsEventEntity)

    @Query("SELECT * FROM analytics_events ORDER BY timestamp ASC LIMIT :limit")
    suspend fun getEvents(limit: Int): List<AnalyticsEventEntity>

    @Query("DELETE FROM analytics_events WHERE id IN (:ids)")
    suspend fun deleteEvents(ids: List<Long>)

    @Query("SELECT * FROM analytics_events ORDER BY timestamp ASC")
    fun observeAllEvents(): Flow<List<AnalyticsEventEntity>>
}