package com.example.reflect.presentation.common

import java.util.Calendar
import java.util.Locale

object DateUtils {
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
            if (today.get(Calendar.YEAR) == currentDate.get(Calendar.YEAR)) {
                "${currentDate.get(Calendar.DAY_OF_MONTH)} " +
                        "${currentDate.getDisplayName(Calendar.MONTH, Calendar.LONG_FORMAT, Locale("ru"))} " +
                        "в ${String.format("%02d", currentDate.get(Calendar.HOUR_OF_DAY))}:${String.format("%02d", currentDate.get(Calendar.MINUTE))}"
            } else {
                "${currentDate.get(Calendar.YEAR)}-${currentDate.get(Calendar.MONTH)+1}-${
                    currentDate.get(Calendar.DAY_OF_MONTH)
                } в ${String.format("%02d", currentDate.get(Calendar.HOUR_OF_DAY))}:${String.format("%02d", currentDate.get(Calendar.MINUTE))}"
            }
        }
    }

    fun dateToString(today: Calendar, currentDate: Calendar): String {
        return if (currentDate.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
            currentDate.get(Calendar.MONTH) == today.get(Calendar.MONTH) &&
            currentDate.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH)) {
            "Сегодня"
        } else if (currentDate.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
            currentDate.get(Calendar.MONTH) == today.get(Calendar.MONTH) &&
            currentDate.get(Calendar.DAY_OF_MONTH) - today.get(Calendar.DAY_OF_MONTH) == -1) {
            "Вчера"
        } else if (currentDate.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
            currentDate.get(Calendar.MONTH) == today.get(Calendar.MONTH) &&
            currentDate.get(Calendar.DAY_OF_MONTH) - today.get(Calendar.DAY_OF_MONTH) == 1) {
            "Завтра"
        }
        else {
            if (today.get(Calendar.YEAR) == currentDate.get(Calendar.YEAR)) {
                "${currentDate.get(Calendar.DAY_OF_MONTH)} " +
                        currentDate.getDisplayName(Calendar.MONTH, Calendar.LONG_FORMAT, Locale(Locale.getDefault().language))
            } else {
                "${currentDate.get(Calendar.YEAR)}-${currentDate.get(Calendar.MONTH)+1}-${currentDate.get(Calendar.DAY_OF_MONTH)}"
            }
        }
    }
}