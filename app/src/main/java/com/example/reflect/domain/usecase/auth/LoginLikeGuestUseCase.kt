package com.example.reflect.domain.usecase.auth

import com.example.reflect.common.RetrofitException
import com.example.reflect.common.RetrofitExceptionHandler
import com.example.reflect.domain.repository.auth.LoginLikeGuestRepository
import com.example.reflect.presentation.screens.login.LoginState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.net.ConnectException
import javax.inject.Inject

class LoginLikeGuestUseCase @Inject constructor(
    private val loginLikeGuestRepository: LoginLikeGuestRepository
) {
    suspend operator fun invoke(): Flow<LoginState> = flow {
        emit(LoginState.Loading)
        try {
            val user = loginLikeGuestRepository.loginLikeGuest()
            emit(LoginState.SuccessGuestLogin(user))
        } catch (e: RetrofitException) {
            emit(LoginState.Error(RetrofitExceptionHandler.getErrorMessage(e), e.code))
        } catch (e: ConnectException) {
            emit(LoginState.Error("Ошибка подключения к интернету", 0))
        } catch (e: Exception) {
            emit(LoginState.Error("Какая то ошибка", -1))
        }
    }
}