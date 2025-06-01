package com.example.analyticssdk.integrations

import android.util.Log
import com.example.mcanalytics.domain.AnalyticsEvent
import com.example.mcanalytics.domain.AnalyticsIntegration

class MixpanelIntegration: AnalyticsIntegration {
    companion object {
        const val integrationName: String = "MixpanelIntegration"
    }
    override suspend fun sendEvent(event: AnalyticsEvent): Boolean {
        Log.d("MixpanelIntegration", "============================")
        Log.d("MixpanelIntegration", "Event: ${event.eventName}")
        Log.d("MixpanelIntegration", "Data: ${event.data?.toString() ?: "No data"}")
        Log.d("MixpanelIntegration", "============================")
        return true
    }
}