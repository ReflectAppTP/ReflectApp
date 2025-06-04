package com.example.reflect.domain.usecase.friendship

import android.util.Log
import com.example.reflect.common.RetrofitException
import com.example.reflect.common.RetrofitExceptionHandler
import com.example.reflect.domain.repository.friendship.RejectFriendshipRepository
import com.example.reflect.presentation.screens.friends.DoWithNotificationState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.net.ConnectException
import javax.inject.Inject

class RejectFriendshipUseCase @Inject constructor(
    private val rejectFriendshipRepository: RejectFriendshipRepository
) {
    suspend operator fun invoke(id: Int): Flow<DoWithNotificationState> = flow {
        emit(DoWithNotificationState.Loading)
        try {
            val response = rejectFriendshipRepository.rejectFriendship(id)
            emit(DoWithNotificationState.Success)
        } catch (e: RetrofitException) {
            emit(DoWithNotificationState.Error(RetrofitExceptionHandler.getErrorMessage(e)))
        } catch (e: ConnectException) {
            emit(DoWithNotificationState.Error("Ошибка подключения к интернету"))
        } catch (e: Exception) {
            Log.d("OkHttp ex", e.message.toString())
            emit(DoWithNotificationState.Error(e.message.toString()))
        }
    }
}