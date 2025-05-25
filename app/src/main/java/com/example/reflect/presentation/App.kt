package com.example.reflect.presentation

import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.reflect.presentation.common.NotificationWorker
import com.vk.id.VKID
import dagger.hilt.android.HiltAndroidApp
import io.appmetrica.analytics.AppMetrica
import io.appmetrica.analytics.AppMetricaConfig
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

@HiltAndroidApp
class App: Application() {
    override fun onCreate() {
        super.onCreate()

        scheduleDailyNotification(this, 17, "AddStateReminder")

        val packageManager = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
        val appMetricaApiKey = packageManager.metaData.getString("APP_METRICA_API_KEY")!!
        val config = AppMetricaConfig.newConfigBuilder(appMetricaApiKey)
            .withLogs()
            .build()
        AppMetrica.activate(this, config)
        AppMetrica.enableActivityAutoTracking(this)

        VKID.init(this)
        VKID.instance.setLocale(Locale("ru"))
        VKID.logsEnabled = true
    }

    private fun scheduleDailyNotification(context: Context, hour: Int, workName: String) {
        val currentTime = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
        }

        val targetTime = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        if (currentTime.after(targetTime)) {
            targetTime.add(Calendar.DAY_OF_MONTH, 1)
        }


        val repeatRequest = PeriodicWorkRequestBuilder<NotificationWorker>(24, TimeUnit.HOURS).build()

        val workManager = WorkManager.getInstance(context)

        workManager.enqueueUniquePeriodicWork(
            workName,
            ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
            repeatRequest
        )
    }
}