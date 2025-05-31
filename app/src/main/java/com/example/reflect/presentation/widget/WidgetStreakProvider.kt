package com.example.reflect.presentation.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import com.example.reflect.R
import com.example.reflect.presentation.mainActivity.MainActivity

class WidgetStreakProvider: AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        Log.d("Widget", "onUpdate delux")
        appWidgetIds.forEach {
            val pendingIntent: PendingIntent = PendingIntent.getActivity(
                context,
                0,
                Intent(context, MainActivity::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val views: RemoteViews = RemoteViews(
                context.packageName,
                R.layout.widget_streak
            ).apply {
                setOnClickPendingIntent(R.id.popupStreakImage, pendingIntent)
            }

            appWidgetManager.updateAppWidget(it, views)
        }
    }

    override fun onEnabled(context: Context?) {
        super.onEnabled(context)
        context?.let {
            updateWidget(it, 0)
        }
    }

    companion object {
        fun updateWidget(context: Context, streakValue: Int) {
            Log.d("Widget", "onUpdate delux in companion")
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val widgetIds = appWidgetManager.getAppWidgetIds(
                ComponentName(context, WidgetStreakProvider::class.java)
            )

            if (widgetIds.isEmpty()) return

            appWidgetManager.notifyAppWidgetViewDataChanged(widgetIds, R.id.popupStreakImage)

            val views = RemoteViews(context.packageName, R.layout.widget_streak).apply {
                setTextViewText(R.id.popupStreakCount, streakValue.toString())
                setImageViewResource(R.id.popupStreakImage, when(streakValue) {
                    0 -> R.drawable.image_cat_streak_0
                    in 1..6 -> R.drawable.image_cat_streak_1
                    else -> R.drawable.image_cat_streak_2
                })
            }

            widgetIds.forEach { appWidgetManager.partiallyUpdateAppWidget(it, views) }
        }

        fun pinWidget(context: Context) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val provider = ComponentName(context, WidgetStreakProvider::class.java)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (appWidgetManager.isRequestPinAppWidgetSupported) {
                    val successCallback = PendingIntent.getBroadcast(
                        context,
                        0,
                        Intent(),
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )

                    appWidgetManager.requestPinAppWidget(provider, null, successCallback)
                }
            } else {
                val intent = Intent().apply {
                    action = AppWidgetManager.ACTION_APPWIDGET_PICK
                    putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, provider)
                }
                context.startActivity(intent)
            }
        }
    }
}