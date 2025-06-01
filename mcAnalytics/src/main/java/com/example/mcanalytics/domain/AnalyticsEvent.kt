package com.example.mcanalytics.domain

data class AnalyticsEvent(
    val eventName: String,
    val data: Map<String, Any?>? = null,
    val integration: String,
    val timestamp: Long = System.currentTimeMillis()
)