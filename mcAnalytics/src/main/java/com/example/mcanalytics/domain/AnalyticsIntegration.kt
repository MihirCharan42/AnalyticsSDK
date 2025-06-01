package com.example.mcanalytics.domain

interface AnalyticsIntegration {
    suspend fun send(events: List<AnalyticsEvent>): Boolean
}