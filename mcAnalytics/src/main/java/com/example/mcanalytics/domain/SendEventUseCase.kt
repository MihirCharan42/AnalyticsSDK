package com.example.mcanalytics.domain

class SendEventUseCase(
    private val repository: AnalyticsRepository,
    private val globalPropertiesProvider: GlobalPropertiesProvider,
    private val integrations: Map<String, AnalyticsIntegration>
) {
    suspend operator fun invoke(
        eventName: String,
        data: Map<String, Any?>?,
        integration: String
    ) {
        val merged = globalPropertiesProvider.mergeWithGlobal(data)
        val event = AnalyticsEvent(eventName, merged, integration)
        val success = integrations[integration]?.sendEvent(event)
        if (success != true) {
            repository.insertEvent(event)
        }
    }
}