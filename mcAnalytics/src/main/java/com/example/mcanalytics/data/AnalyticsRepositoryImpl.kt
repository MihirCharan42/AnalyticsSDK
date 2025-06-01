package com.example.mcanalytics.data

import com.example.mcanalytics.domain.AnalyticsEvent
import com.example.mcanalytics.domain.AnalyticsRepository
import com.google.gson.Gson

class AnalyticsRepositoryImpl(
    private val dao: EventDao,
    private val gson: Gson
) : AnalyticsRepository {
    override suspend fun insertEvent(event: AnalyticsEvent) {
        dao.insert(event.toEntity(gson))
    }

    override suspend fun getPendingEvents(limit: Int): List<AnalyticsEvent> {
        return dao.getEvents(limit).map { it.toDomain(gson) }
    }

    override suspend fun deleteEvents(eventIds: List<Long>) {
        dao.deleteEvents(eventIds)
    }
}