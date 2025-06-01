package com.example.reflect.presentation.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import com.example.reflect.R
import com.example.reflect.presentation.mainActivity.MainActivity

class WidgetStateProvider: AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        appWidgetIds.forEach {
            val pendingIntent: PendingIntent = PendingIntent.getActivity(
                context,
                0,
                Intent(context, MainActivity::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val views: RemoteViews = RemoteViews(
                context.packageName,
                R.layout.widget_state
            ).apply {
                setOnClickPendingIntent(R.id.widgetStateRoot, pendingIntent)
            }

            appWidgetManager.updateAppWidget(it, views)
        }
    }

    override fun onEnabled(context: Context?) {
        super.onEnabled(context)
        context?.let {
            updateWidget(it, null)
        }
    }

    companion object {
        fun updateWidget(context: Context, stateValue: Int?) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val widgetIds = appWidgetManager.getAppWidgetIds(
                ComponentName(context, WidgetStateProvider::class.java)
            )

            if (widgetIds.isEmpty()) return

            val views = RemoteViews(context.packageName, R.layout.widget_state).apply {
                when (stateValue) {
                    in 0..1 -> {
                        setTextViewText(R.id.widgetStateTitle, context.resources.getString(R.string.widgetStateTitle))
                        setTextViewText(R.id.widgetStateDescription, context.resources.getString(R.string.awfulState))
                        setImageViewResource(R.id.widgetStateImage, R.drawable.ic_state_image_1)
                    }
                    in 2..3 -> {
                        setTextViewText(R.id.widgetStateTitle, context.resources.getString(R.string.widgetStateTitle))
                        setTextViewText(R.id.widgetStateDescription, context.resources.getString(R.string.badState))
                        setImageViewResource(R.id.widgetStateImage, R.drawable.ic_state_image_2)
                    }
                    in 4..6 -> {
                        setTextViewText(R.id.widgetStateTitle, context.resources.getString(R.string.widgetStateTitle))
                        setTextViewText(R.id.widgetStateDescription, context.resources.getString(R.string.normalState))
                        setImageViewResource(R.id.widgetStateImage, R.drawable.ic_state_image_3)
                    }
                    in 7..8 -> {
                        setTextViewText(R.id.widgetStateTitle, context.resources.getString(R.string.widgetStateTitle))
                        setTextViewText(R.id.widgetStateDescription, context.resources.getString(R.string.goodState))
                        setImageViewResource(R.id.widgetStateImage, R.drawable.ic_state_image_4)
                    }
                    in 9..10 -> {
                        setTextViewText(R.id.widgetStateTitle, context.resources.getString(R.string.widgetStateTitle))
                        setTextViewText(R.id.widgetStateDescription, context.resources.getString(R.string.excellentState))
                        setImageViewResource(R.id.widgetStateImage, R.drawable.ic_state_image_5)
                    }
                    else -> {
                        setTextViewText(R.id.widgetStateTitle, "Сегодня ещё")
                        setTextViewText(R.id.widgetStateDescription, "нет записей")
                        setImageViewResource(R.id.widgetStateImage, R.drawable.ic_empty_widget_state)
                    }
                }
            }

            widgetIds.forEach { appWidgetManager.updateAppWidget(it, views) }
        }

        fun pinWidget(context: Context) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val provider = ComponentName(context, WidgetStateProvider::class.java)

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