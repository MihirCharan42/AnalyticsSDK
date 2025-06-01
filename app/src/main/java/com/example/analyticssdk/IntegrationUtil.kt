package com.example.analyticssdk

import com.example.analyticssdk.integrations.DevIntegration
import com.example.analyticssdk.integrations.FirebaseIntegration
import com.example.analyticssdk.integrations.MixpanelIntegration
import com.example.mcanalytics.domain.AnalyticsIntegration

object IntegrationUtil {
    fun getIntegration(): Map<String, AnalyticsIntegration> = mapOf(
        DevIntegration.integrationName to DevIntegration(),
        MixpanelIntegration.integrationName to MixpanelIntegration(),
        FirebaseIntegration.integrationName to FirebaseIntegration(),
    )
}