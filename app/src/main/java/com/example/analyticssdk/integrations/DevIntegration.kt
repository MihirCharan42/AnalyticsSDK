package com.example.analyticssdk.integrations

import android.util.Log
import com.example.mcanalytics.domain.AnalyticsEvent
import com.example.mcanalytics.domain.AnalyticsIntegration

class DevIntegration: AnalyticsIntegration {
    companion object {
        const val integrationName: String = "DevIntegration"
    }
    override suspend fun sendEvent(event: AnalyticsEvent): Boolean {
        Log.d("DevIntegration", "============================")
        Log.d("DevIntegration", "Event: ${event.eventName}")
        Log.d("DevIntegration", "Data: ${event.data?.toString() ?: "No data"}")
        Log.d("DevIntegration", "============================")
        return true
    }
}