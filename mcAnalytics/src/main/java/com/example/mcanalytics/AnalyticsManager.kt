package com.example.mcanalytics

import android.content.Context
import com.example.mcanalytics.data.AnalyticsDatabase
import com.example.mcanalytics.data.AnalyticsRepositoryImpl
import com.example.mcanalytics.domain.GlobalPropertiesProvider
import com.example.mcanalytics.domain.SendEventUseCase
import com.example.mcanalytics.domain.SyncEventsUseCase
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

object AnalyticsManager {
    private lateinit var sendEventUseCase: SendEventUseCase
    private lateinit var syncEventsUseCase: SyncEventsUseCase
    private lateinit var globalPropertiesProvider: GlobalPropertiesProvider
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    fun init(
        context: Context,
        config: AnalyticsConfig
    ) {
        val db = AnalyticsDatabase.getInstance(context)
        val gson = Gson()
        val repository = AnalyticsRepositoryImpl(db.eventDao(), gson)
        globalPropertiesProvider = GlobalPropertiesProvider()
        sendEventUseCase = SendEventUseCase(repository, globalPropertiesProvider, config.integrations)
        syncEventsUseCase = SyncEventsUseCase(repository, config.integrations)
        schedulePeriodicSync(context, config.syncIntervalHours)
    }

    fun sendEvent(
        name: String,
        data: Map<String, Any?>? = null,
        integration: String
    ) {
        scope.launch {
            sendEventUseCase(name, data, integration)
        }
    }

    fun syncNow() {
        scope.launch {
            syncEventsUseCase()
        }
    }

    fun setGlobalProperties(properties: Map<String, Any?>) =
        globalPropertiesProvider.set(properties)

    fun unsetAllGlobalProperties() =
        globalPropertiesProvider.unsetAll()

    fun unsetGlobalProperty(keys: List<String>) =
        globalPropertiesProvider.unset(keys)

    private fun schedulePeriodicSync(context: Context, intervalHours: Long = 1) {
        AnalyticsSyncWorker.schedulePeriodic(context, intervalHours)
    }
}

