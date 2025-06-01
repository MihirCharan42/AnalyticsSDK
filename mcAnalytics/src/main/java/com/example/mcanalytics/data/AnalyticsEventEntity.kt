package com.example.mcanalytics.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mcanalytics.domain.AnalyticsEvent
import kotlinx.serialization.json.Json

@Entity(tableName = "analytics_events")
data class AnalyticsEventEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val eventName: String,
    val dataJson: String,
    val integration: String,
    val timestamp: Long
)

fun AnalyticsEvent.toEntity(): AnalyticsEventEntity = AnalyticsEventEntity(
    eventName = eventName,
    dataJson = Json.encodeToString(data),
    integration = integration,
    timestamp = timestamp
)

fun AnalyticsEventEntity.toDomain(): AnalyticsEvent = AnalyticsEvent(
    eventName = eventName,
    data = Json.decodeFromString(dataJson),
    integration = integration,
    timestamp = timestamp
)