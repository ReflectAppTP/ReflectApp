package com.example.reflect.common

import okhttp3.ResponseBody

data class RetrofitException(
    val code: Int,
    override val message: String,
    val errorBody: ResponseBody?
) : Exception(message)