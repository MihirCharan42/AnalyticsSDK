package com.example.mcanalytics.domain

import kotlinx.coroutines.flow.Flow

interface AnalyticsRepository {
    suspend fun insertEvent(event: AnalyticsEvent)
    suspend fun getPendingEvents(limit: Int): List<AnalyticsEvent>
    suspend fun deleteEvents(eventIds: List<Long>)
    fun observeEventQueue(): Flow<List<AnalyticsEvent>>
}