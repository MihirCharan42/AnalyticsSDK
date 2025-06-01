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
            val successfullySentEvents  = mutableListOf<AnalyticsEvent>()
            events.forEach { event ->
                val success = integration.sendEvent(event)
                if (success) {
                    successfullySentEvents.add(event)
                }
            }
            repository.deleteEvents(successfullySentEvents.map { it.timestamp })
        }
    }
}