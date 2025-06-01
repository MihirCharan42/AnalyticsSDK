package com.example.mcanalytics.domain

data class AnalyticsEvent(
    val eventName: String,
    val data: Map<String, Any?>,
    val integration: String,
    val timestamp: Long = System.currentTimeMillis()
)