package com.example.mcanalytics

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.mcanalytics.data.AnalyticsDatabase
import com.example.mcanalytics.data.AnalyticsRepositoryImpl
import com.example.mcanalytics.domain.AnalyticsIntegration
import com.example.mcanalytics.domain.SyncEventsUseCase
import java.util.concurrent.TimeUnit

class AnalyticsSyncWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val context = applicationContext
            val db = AnalyticsDatabase.getInstance(context)
            val repository = AnalyticsRepositoryImpl(db.eventDao())

            // Replace with real integration implementations
            val integrations = mapOf<String, AnalyticsIntegration>()
            val syncUseCase = SyncEventsUseCase(repository, integrations)
            syncUseCase()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    companion object {
        const val WORK_NAME = "AnalyticsSyncWork"

        fun schedulePeriodic(context: Context, intervalHours: Long = 1) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .build()

            val workRequest = PeriodicWorkRequestBuilder<AnalyticsSyncWorker>(
                intervalHours, TimeUnit.HOURS
            )
                .setConstraints(constraints)
                .build()

            WorkManager.getInstance(context)
                .enqueueUniquePeriodicWork(
                    WORK_NAME,
                    ExistingPeriodicWorkPolicy.KEEP,
                    workRequest
                )
        }
    }
}