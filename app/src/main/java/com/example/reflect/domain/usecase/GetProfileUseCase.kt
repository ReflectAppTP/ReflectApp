package com.example.reflect.domain.usecase

import com.example.reflect.common.RetrofitException
import com.example.reflect.common.RetrofitExceptionHandler
import com.example.reflect.domain.repository.GetProfileRepository
import com.example.reflect.presentation.screens.login.LoginState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.net.ConnectException
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(
    private val getProfileRepository: GetProfileRepository
) {
    suspend operator fun invoke(
        accessToken: String
    ): Flow<LoginState> = flow {
        try {
            val user = getProfileRepository.getUser(accessToken)
            emit(LoginState.SuccessGetProfile(user))
        } catch (e: RetrofitException) {
            emit(LoginState.Error(RetrofitExceptionHandler.getErrorMessage(e)))
        } catch (e: ConnectException) {
            emit(LoginState.Error("Ошибка подключения к интернету"))
        }
    }
}