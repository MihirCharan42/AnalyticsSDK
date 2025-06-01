package com.example.mcanalytics

import com.example.mcanalytics.domain.AnalyticsIntegration

data class AnalyticsConfig(
    val integrations: Map<String, AnalyticsIntegration>,
    val syncIntervalHours: Long = 1
)