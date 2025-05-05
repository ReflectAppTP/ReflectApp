package com.example.reflect.domain.usecase

import com.example.reflect.common.RetrofitException
import com.example.reflect.common.RetrofitExceptionHandler
import com.example.reflect.domain.repository.RefreshRepository
import com.example.reflect.presentation.mainActivity.GetProfileState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.net.ConnectException
import javax.inject.Inject

class RefreshUseCase @Inject constructor(
    private val refreshRepository: RefreshRepository
) {
    suspend operator fun invoke(
        refreshToken: String
    ): Flow<GetProfileState> = flow {
        emit(GetProfileState.Loading)
        try {
            val loginModel = refreshRepository.getAccessToken(refreshToken)
            emit(GetProfileState.Success(loginModel))
        } catch (e: RetrofitException) {
            emit(GetProfileState.Error(RetrofitExceptionHandler.getErrorMessage(e), e.code))
        } catch (e: ConnectException) {
            emit(GetProfileState.Error("Ошибка подлючения к интернету", 0))
        }
    }
}