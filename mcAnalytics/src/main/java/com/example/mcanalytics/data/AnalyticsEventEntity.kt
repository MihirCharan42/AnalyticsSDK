package com.example.mcanalytics.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mcanalytics.domain.AnalyticsEvent
import com.google.gson.Gson

@Entity(tableName = "analytics_events")
data class AnalyticsEventEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val eventName: String,
    val dataJson: String,
    val integration: String,
    val timestamp: Long
)

fun AnalyticsEvent.toEntity(gson: Gson): AnalyticsEventEntity = AnalyticsEventEntity(
    eventName = eventName,
    dataJson = data?.let { gson.toJson(it) } ?: "{}",
    integration = integration,
    timestamp = timestamp
)

fun AnalyticsEventEntity.toDomain(gson: Gson): AnalyticsEvent = AnalyticsEvent(
    eventName = eventName,
    data = gson.fromJson<Map<String, Any?>>(dataJson, Map::class.java),
    integration = integration,
    timestamp = timestamp
)