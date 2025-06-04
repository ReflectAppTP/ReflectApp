package com.example.reflect.presentation.common

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateUtils {

    @SuppressLint("DefaultLocale")
    fun creationDateToString(today: Calendar, currentDate: Calendar): String {
        return if (currentDate.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
            currentDate.get(Calendar.MONTH) == today.get(Calendar.MONTH) &&
            currentDate.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH)) {
            "Сегодня в ${String.format("%02d", currentDate.get(Calendar.HOUR_OF_DAY))}:${String.format("%02d", currentDate.get(Calendar.MINUTE))}"
        } else if (currentDate.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
            currentDate.get(Calendar.MONTH) == today.get(Calendar.MONTH) &&
            currentDate.get(Calendar.DAY_OF_MONTH) - today.get(Calendar.DAY_OF_MONTH) == -1) {
            "Вчера в ${String.format("%02d", currentDate.get(Calendar.HOUR_OF_DAY))}:${String.format("%02d", currentDate.get(Calendar.MINUTE))}"
        } else {
            // 12 апреля в 03:03
            if (today.get(Calendar.YEAR) == currentDate.get(Calendar.YEAR)) {
                "${currentDate.get(Calendar.DAY_OF_MONTH)} " +
                        "${currentDate.getDisplayName(Calendar.MONTH, Calendar.LONG_FORMAT, Locale("ru"))} " +
                        "в ${String.format("%02d", currentDate.get(Calendar.HOUR_OF_DAY))}:${String.format("%02d", currentDate.get(Calendar.MINUTE))}"
            } else {
                // 2025-10-05 в 19:30
                "${currentDate.get(Calendar.YEAR)}-${String.format("%02d", currentDate.get(Calendar.MONTH)+1)}-${String.format("%02d", currentDate.get(Calendar.DAY_OF_MONTH))}" +
                        " в ${String.format("%02d", currentDate.get(Calendar.HOUR_OF_DAY))}:${String.format("%02d", currentDate.get(Calendar.MINUTE))}"
            }
        }
    }

    @SuppressLint("DefaultLocale")
    fun dateToString(today: Calendar, currentDate: Calendar): String {
        return if (currentDate.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
            currentDate.get(Calendar.MONTH) == today.get(Calendar.MONTH) &&
            currentDate.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH)) {
            "Сегодня"
        } else if (currentDate.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
            currentDate.get(Calendar.MONTH) == today.get(Calendar.MONTH) &&
            currentDate.get(Calendar.DAY_OF_MONTH) - today.get(Calendar.DAY_OF_MONTH) == -1) {
            "Вчера"
        }
        else {
            // 9 апреля 2025
            "${currentDate.get(Calendar.DAY_OF_MONTH)} " +
                    currentDate.getDisplayName(Calendar.MONTH, Calendar.LONG_FORMAT, Locale(Locale.getDefault().language)) +
                    " ${currentDate.get(Calendar.YEAR)}"
        }
    }

    fun getStringFromDate(time: Date): String  {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(time)
    }

    fun getWeekRange(today: Calendar, selectedDate: Calendar) : String {
        return if (today.get(Calendar.DAY_OF_MONTH) < selectedDate.get(Calendar.DAY_OF_MONTH)) {
            // 24 фев - 1 мар 2025
            "${selectedDate.get(Calendar.DAY_OF_MONTH)} " +
                    selectedDate.getDisplayName(Calendar.MONTH, Calendar.SHORT_FORMAT, Locale(Locale.getDefault().language)) +
                    " - " +
                    "${today.get(Calendar.DAY_OF_MONTH)} " +
                    today.getDisplayName(Calendar.MONTH, Calendar.SHORT_FORMAT, Locale(Locale.getDefault().language)) +
                    " ${today.get(Calendar.YEAR)}"
        } else {
            // 1 - 8 мар 2025
            "${selectedDate.get(Calendar.DAY_OF_MONTH)}" +
                    " - " +
                    "${today.get(Calendar.DAY_OF_MONTH)} " +
                    today.getDisplayName(Calendar.MONTH, Calendar.SHORT_FORMAT, Locale(Locale.getDefault().language)) +
                    " ${today.get(Calendar.YEAR)}"
        }

    }

    fun getMonthRange(today: Calendar, selectedDate: Calendar) : String {
        return if (today.get(Calendar.MONTH) < selectedDate.get(Calendar.MONTH)) {
            // 12 декабря 2024 - 12 января 2025
            "${selectedDate.get(Calendar.DAY_OF_MONTH)} " +
                    selectedDate.getDisplayName(Calendar.MONTH, Calendar.SHORT_FORMAT, Locale(Locale.getDefault().language)) +
                    " ${today.get(Calendar.YEAR)}" +
                    " - " +
                    "${today.get(Calendar.DAY_OF_MONTH)} " +
                    today.getDisplayName(Calendar.MONTH, Calendar.SHORT_FORMAT, Locale(Locale.getDefault().language)) +
                    " ${today.get(Calendar.YEAR)}"
        } else {
            // 12 мая - 12 июня 2025
            "${selectedDate.get(Calendar.DAY_OF_MONTH)} " +
                    selectedDate.getDisplayName(Calendar.MONTH, Calendar.SHORT_FORMAT, Locale(Locale.getDefault().language)) +
                    " - " +
                    "${today.get(Calendar.DAY_OF_MONTH)} " +
                    today.getDisplayName(Calendar.MONTH, Calendar.SHORT_FORMAT, Locale(Locale.getDefault().language)) +
                    " ${today.get(Calendar.YEAR)}"
        }
    }

    fun getYearRange(today: Calendar, selectedDate: Calendar) : String {
        // апрель 2024 - апрель 2025
        return selectedDate.getDisplayName(Calendar.MONTH, Calendar.SHORT_STANDALONE, Locale(Locale.getDefault().language))!! +
                " ${selectedDate.get(Calendar.YEAR)}" +
                " - " +
                today.getDisplayName(Calendar.MONTH, Calendar.SHORT_STANDALONE, Locale(Locale.getDefault().language)) +
                " ${today.get(Calendar.YEAR)}"
    }
}