package com.example.reflect.common

object RetrofitExceptionHandler {

    fun getErrorMessage(e: RetrofitException): String =
        when (e.code) {
//            401 -> "Такой пользователь уже зарегистрирован"
            400 -> "Неверный запрос"
            401 -> "Такого пользователя не существует"
            404 -> "Не найдено" // Переделать
            429 -> "Повторите попытку позже"
            500 -> "Ошибка на сервере"
            else -> "Что-то пошло не так"
        }
}