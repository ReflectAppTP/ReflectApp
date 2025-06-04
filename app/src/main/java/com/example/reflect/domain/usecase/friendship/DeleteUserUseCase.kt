package com.example.reflect.domain.usecase.friendship

import android.util.Log
import com.example.reflect.common.RetrofitException
import com.example.reflect.common.RetrofitExceptionHandler
import com.example.reflect.domain.repository.friendship.DeleteUserRepository
import com.example.reflect.presentation.screens.profile.DeleteUserState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.net.ConnectException
import javax.inject.Inject

class DeleteUserUseCase @Inject constructor(
    private val deleteUserRepository: DeleteUserRepository
) {
    suspend operator fun invoke(): Flow<DeleteUserState> = flow {
        emit(DeleteUserState.Loading)
        try {
            val response = deleteUserRepository.deleteUser()
            emit(DeleteUserState.Success)
        } catch (e: RetrofitException) {
            emit(DeleteUserState.Error(RetrofitExceptionHandler.getErrorMessage(e)))
        } catch (e: ConnectException) {
            emit(DeleteUserState.Error("Ошибка подключения к интернету"))
        } catch (e: Exception) {
            emit(DeleteUserState.Success)
        }
    }
}