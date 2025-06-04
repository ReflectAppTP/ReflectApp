package com.example.reflect.domain.usecase.friendship

import com.example.reflect.common.RetrofitException
import com.example.reflect.common.RetrofitExceptionHandler
import com.example.reflect.domain.repository.friendship.ChangePasswordRepository
import com.example.reflect.presentation.screens.profile.UpdateProfileState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.net.ConnectException
import javax.inject.Inject

class ChangePasswordUseCase @Inject constructor(
    private val changePasswordRepository: ChangePasswordRepository
) {
    suspend operator fun invoke(oldPassword: String, newPassword: String): Flow<UpdateProfileState> = flow {
        emit(UpdateProfileState.Loading)
        try {
            val response = changePasswordRepository.updatePassword(oldPassword, newPassword)
            emit(UpdateProfileState.Success)
        } catch (e: RetrofitException) {
            emit(UpdateProfileState.Error(if (e.code == 400) "Неверный пароль" else RetrofitExceptionHandler.getErrorMessage(e)))
        } catch (e: ConnectException) {
            emit(UpdateProfileState.Error("Ошибка подключения к интернету"))
        } catch (e: Exception) {
            emit(UpdateProfileState.Error("Какая то ошибка"))
        }
    }
}