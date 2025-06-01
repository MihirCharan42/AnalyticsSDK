package com.example.mcanalytics

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.mcanalytics.domain.AnalyticsIntegration
import com.example.mcanalytics.domain.AnalyticsRepository
import com.example.mcanalytics.domain.GlobalPropertiesProvider
import com.example.mcanalytics.domain.SendEventUseCase
import com.example.mcanalytics.domain.SyncEventsUseCase
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
        repository: AnalyticsRepository,
        integrations: Map<String, AnalyticsIntegration>
    ) {
        globalPropertiesProvider = GlobalPropertiesProvider()
        sendEventUseCase = SendEventUseCase(repository, globalPropertiesProvider)
        syncEventsUseCase = SyncEventsUseCase(repository, integrations)
    }

    fun sendEvent(
        name: String,
        data: Map<String, Any?>,
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

    fun unsetGlobalProperties(keys: List<String>) =
        globalPropertiesProvider.unset(keys)

    fun schedulePeriodicSync(context: Context, intervalHours: Long = 1) {
        AnalyticsSyncWorker.schedulePeriodic(context, intervalHours)
    }

    fun triggerImmediateSync(context: Context) {
        val workRequest = OneTimeWorkRequestBuilder<AnalyticsSyncWorker>().build()
        WorkManager.getInstance(context).enqueue(workRequest)
    }
}

