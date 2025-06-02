package com.example.reflect.domain.usecase.friendship

import android.util.Log
import com.example.reflect.common.RetrofitException
import com.example.reflect.common.RetrofitExceptionHandler
import com.example.reflect.domain.repository.friendship.GetFriendsNotificationRepository
import com.example.reflect.presentation.screens.friends.GetFriendsNotificationsState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.net.ConnectException
import javax.inject.Inject

class GetFriendsNotificationUseCase @Inject constructor(
    private val getFriendsNotificationRepository: GetFriendsNotificationRepository
) {
    suspend operator fun invoke(): Flow<GetFriendsNotificationsState> = flow {
        emit(GetFriendsNotificationsState.Loading)
        try {
            val response = getFriendsNotificationRepository.getFriendshipNotification()
            if (response.isEmpty()) {
                emit(GetFriendsNotificationsState.EmptyContent)
            } else {
                emit(GetFriendsNotificationsState.Success(response))
            }
        } catch (e: RetrofitException) {
            emit(GetFriendsNotificationsState.Error(RetrofitExceptionHandler.getErrorMessage(e)))
        } catch (e: ConnectException) {
            emit(GetFriendsNotificationsState.Error("Ошибка подключения к интернету"))
        } catch (e: Exception) {
            Log.d("OkHttp ex", e.message.toString())
            emit(GetFriendsNotificationsState.Error(e.message.toString()))
        }
    }
}