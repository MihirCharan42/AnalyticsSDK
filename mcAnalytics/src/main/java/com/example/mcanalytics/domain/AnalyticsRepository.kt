package com.example.mcanalytics.domain

interface AnalyticsRepository {
    suspend fun insertEvent(event: AnalyticsEvent)
    suspend fun getPendingEvents(limit: Int): List<AnalyticsEvent>
    suspend fun deleteEvents(eventIds: List<Long>)
}