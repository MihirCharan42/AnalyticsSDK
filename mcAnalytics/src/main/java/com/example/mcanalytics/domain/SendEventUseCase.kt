package com.example.mcanalytics.domain

class SendEventUseCase(
    private val repository: AnalyticsRepository,
    private val globalPropertiesProvider: GlobalPropertiesProvider
) {
    suspend operator fun invoke(
        eventName: String,
        data: Map<String, Any?>,
        integration: String
    ) {
        val merged = globalPropertiesProvider.mergeWithGlobal(data)
        val event = AnalyticsEvent(eventName, merged, integration)
        repository.insertEvent(event)
    }
}