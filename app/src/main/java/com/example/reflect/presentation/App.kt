package com.example.reflect.presentation

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import io.appmetrica.analytics.AppMetrica
import io.appmetrica.analytics.AppMetricaConfig

@HiltAndroidApp
class App: Application() {
    override fun onCreate() {
        super.onCreate()

        // TODO: вынести апи ключ в другое место
        val config = AppMetricaConfig.newConfigBuilder("d067fc5b-c791-460c-8b23-fd630d72d6c6")
            .withLogs()
            .build()
        AppMetrica.activate(this, config)
        AppMetrica.enableActivityAutoTracking(this)
    }
}