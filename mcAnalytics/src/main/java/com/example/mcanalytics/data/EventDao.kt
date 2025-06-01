package com.example.mcanalytics.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface EventDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(event: AnalyticsEventEntity)

    @Query("SELECT * FROM analytics_events ORDER BY timestamp ASC LIMIT :limit")
    fun getEvents(limit: Int): List<AnalyticsEventEntity>

    @Query("DELETE FROM analytics_events WHERE id IN (:ids)")
    fun deleteEvents(ids: List<Long>)
}