package com.example.reflect.domain.usecase.auth

import com.example.reflect.common.RetrofitException
import com.example.reflect.common.RetrofitExceptionHandler
import com.example.reflect.domain.repository.auth.RegistrationRepository
import com.example.reflect.presentation.screens.registration.RegistrationState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.net.ConnectException
import javax.inject.Inject

class RegistrationUseCase @Inject constructor(
    private val registrationRepository: RegistrationRepository
) {
    suspend operator fun invoke(
        username: String,
        email: String,
        password: String
    ): Flow<RegistrationState> = flow {
        emit(RegistrationState.Loading)
        try {
            val user = registrationRepository.register(username, email, password)
            emit(RegistrationState.Success(user))
        } catch (e: RetrofitException) {
            emit(RegistrationState.Error(if (e.code == 400) "Пользователь с таким логином существует" else RetrofitExceptionHandler.getErrorMessage(e)))
        } catch (e: ConnectException) {
            emit(RegistrationState.Error("Ошибка подлючения к интернету"))
        } catch (e: Exception) {
            emit(RegistrationState.Error("Какая то ошибка"))
        }
    }
}