package com.example.mcanalytics.domain

class SyncEventsUseCase(
    private val repository: AnalyticsRepository,
    private val integrations: Map<String, AnalyticsIntegration>
) {
    suspend operator fun invoke() {
        val pendingEvents = repository.getPendingEvents(limit = 50)
        val grouped = pendingEvents.groupBy { it.integration }

        for ((integrationKey, events) in grouped) {
            val integration = integrations[integrationKey] ?: continue
            val success = integration.send(events)
            if (success) {
                repository.deleteEvents(events.map { it.timestamp })
            }
        }
    }
}