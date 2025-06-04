package com.example.reflect.domain.usecase.friendship

import com.example.reflect.common.RetrofitException
import com.example.reflect.common.RetrofitExceptionHandler
import com.example.reflect.domain.repository.friendship.SendFriendshipRequestRepository
import com.example.reflect.presentation.screens.friends.SendFriendshipRequestState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.net.ConnectException
import javax.inject.Inject

class SendFriendshipRequestUseCase @Inject constructor(
    private val sendFriendshipRequestRepository: SendFriendshipRequestRepository
) {
    suspend operator fun invoke(toUserId: Int): Flow<SendFriendshipRequestState> = flow {
        emit(SendFriendshipRequestState.Loading)
        try {
            val response = sendFriendshipRequestRepository.sendFriendshipRequest(toUserId)
            emit(SendFriendshipRequestState.Success)
        } catch (e: RetrofitException) {
            emit(SendFriendshipRequestState.Error(RetrofitExceptionHandler.getErrorMessage(e)))
        } catch (e: ConnectException) {
            emit(SendFriendshipRequestState.Error("Ошибка подключения к интернету"))
        } catch (e: Exception) {
            emit(SendFriendshipRequestState.Error(e.message.toString()))
        }
    }
}