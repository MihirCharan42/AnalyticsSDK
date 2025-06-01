package com.example.analyticssdk.integrations

import android.util.Log
import com.example.mcanalytics.domain.AnalyticsEvent
import com.example.mcanalytics.domain.AnalyticsIntegration

class FirebaseIntegration: AnalyticsIntegration {
    companion object {
        const val integrationName: String = "FirebaseIntegration"
    }
    override suspend fun sendEvent(event: AnalyticsEvent): Boolean {
        Log.d("FirebaseIntegration", "============================")
        Log.d("FirebaseIntegration", "Event: ${event.eventName}")
        Log.d("FirebaseIntegration", "Data: ${event.data?.toString() ?: "No data"}")
        Log.d("FirebaseIntegration", "============================")
        return true
    }
}