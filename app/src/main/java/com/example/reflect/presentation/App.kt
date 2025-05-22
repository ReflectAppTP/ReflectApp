package com.example.reflect.presentation

import android.app.Application
import android.content.pm.PackageManager
import dagger.hilt.android.HiltAndroidApp
import io.appmetrica.analytics.AppMetrica
import io.appmetrica.analytics.AppMetricaConfig

@HiltAndroidApp
class App: Application() {
    override fun onCreate() {
        super.onCreate()

        val packageManager = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
        val appMetricaApiKey = packageManager.metaData.getString("APP_METRICA_API_KEY")!!
        val config = AppMetricaConfig.newConfigBuilder(appMetricaApiKey)
            .withLogs()
            .build()
        AppMetrica.activate(this, config)
        AppMetrica.enableActivityAutoTracking(this)
    }
}