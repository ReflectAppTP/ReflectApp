package com.example.reflect.domain.usecase

import com.example.reflect.common.RetrofitException
import com.example.reflect.common.RetrofitExceptionHandler
import com.example.reflect.domain.repository.LoginRepository
import com.example.reflect.presentation.screens.login.LoginState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.net.ConnectException
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String
    ): Flow<LoginState> = flow {
        emit(LoginState.Loading)
        try {
          val loginModel = loginRepository.login(email, password)
          emit(LoginState.Success(loginModel))
        } catch (e: RetrofitException) {
            emit(LoginState.Error(RetrofitExceptionHandler.getErrorMessage(e)))
        } catch (e: ConnectException) {
            emit(LoginState.Error("Ошибка подлючения к интернету"))
        }
    }
}