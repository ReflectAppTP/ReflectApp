package com.example.reflect.domain.usecase.auth

import android.util.Log
import com.example.reflect.common.RetrofitException
import com.example.reflect.common.RetrofitExceptionHandler
import com.example.reflect.domain.repository.auth.RegistrationFromGuestRepository
import com.example.reflect.presentation.screens.registration.RegistrationState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.net.ConnectException
import javax.inject.Inject

class RegistrationFromGuestUseCase @Inject constructor(
    private val registrationFromGuestRepository: RegistrationFromGuestRepository
) {
    suspend operator fun invoke(
        username: String,
        email: String,
        password: String
    ): Flow<RegistrationState> = flow {
        emit(RegistrationState.Loading)
        try {
            val user = registrationFromGuestRepository.registerFromGuest(username, email, password)
            Log.d("Okhttpt", user.toString())
            emit(RegistrationState.Success(user))
        } catch (e: RetrofitException) {
            emit(RegistrationState.Error(if (e.code == 400) "Пользователь с таким логином существует" else RetrofitExceptionHandler.getErrorMessage(e)))
        } catch (e: ConnectException) {
            emit(RegistrationState.Error("Ошибка подлючения к интернету"))
        } catch (e: Exception) {
            Log.d("Okhttpt", e.message.toString())
            emit(RegistrationState.Error("Какая то ошибка"))
        }
    }
}