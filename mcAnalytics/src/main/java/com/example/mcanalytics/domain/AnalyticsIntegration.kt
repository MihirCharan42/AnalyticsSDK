package com.example.mcanalytics.domain

interface AnalyticsIntegration {
    suspend fun sendEvent(event: AnalyticsEvent): Boolean
}