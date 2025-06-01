package com.example.mcanalytics.data

import com.example.mcanalytics.domain.AnalyticsEvent
import com.example.mcanalytics.domain.AnalyticsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AnalyticsRepositoryImpl(
    private val dao: EventDao
) : AnalyticsRepository {
    override suspend fun insertEvent(event: AnalyticsEvent) {
        dao.insert(event.toEntity())
    }

    override suspend fun getPendingEvents(limit: Int): List<AnalyticsEvent> {
        return dao.getEvents(limit).map { it.toDomain() }
    }

    override suspend fun deleteEvents(eventIds: List<Long>) {
        dao.deleteEvents(eventIds)
    }

    override fun observeEventQueue(): Flow<List<AnalyticsEvent>> {
        return dao.observeAllEvents().map { it.map { entity -> entity.toDomain() } }
    }
}