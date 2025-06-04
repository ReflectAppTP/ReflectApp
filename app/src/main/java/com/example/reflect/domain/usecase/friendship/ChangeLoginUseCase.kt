package com.example.reflect.domain.usecase.friendship

import com.example.reflect.common.RetrofitException
import com.example.reflect.common.RetrofitExceptionHandler
import com.example.reflect.domain.repository.friendship.ChangeLoginRepository
import com.example.reflect.presentation.screens.profile.UpdateProfileState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.net.ConnectException
import javax.inject.Inject

class ChangeLoginUseCase @Inject constructor(
    private val changeLoginRepository: ChangeLoginRepository
) {
    suspend operator fun invoke(login: String): Flow<UpdateProfileState> = flow {
        try {
            val response = changeLoginRepository.updateLogin(login)
            emit(UpdateProfileState.Success)
        } catch (e: RetrofitException) {
            emit(UpdateProfileState.Error(if (e.code == 400) "Пользователь с таким логином существует" else RetrofitExceptionHandler.getErrorMessage(e)))
        } catch (e: ConnectException) {
            emit(UpdateProfileState.Error("Ошибка подключения к интернету"))
        } catch (e: Exception) {
            emit(UpdateProfileState.Error("Какая то ошибка"))
        }
    }
}