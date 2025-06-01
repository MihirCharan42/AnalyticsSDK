package com.example.analyticssdk

import android.app.Application
import com.example.mcanalytics.AnalyticsConfig
import com.example.mcanalytics.AnalyticsManager

class AnalyticsDemoApp : Application() {
    override fun onCreate() {
        super.onCreate()
        AnalyticsManager.init(this, AnalyticsConfig(IntegrationUtil.getIntegration()))
    }
}