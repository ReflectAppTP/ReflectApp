package com.example.reflect.domain.usecase.friendship

import android.util.Log
import com.example.reflect.common.RetrofitException
import com.example.reflect.common.RetrofitExceptionHandler
import com.example.reflect.domain.repository.friendship.GetFriendsListRepository
import com.example.reflect.presentation.screens.friends.GetFriendsState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.net.ConnectException
import javax.inject.Inject

class GetFriendsListUseCase @Inject constructor(
    private val getFriendsListRepository: GetFriendsListRepository
) {
    suspend operator fun invoke(): Flow<GetFriendsState> = flow {
        emit(GetFriendsState.Loading)
        try {
            val friends = getFriendsListRepository.getFriendList()
            if (friends.isEmpty()) {
                emit(GetFriendsState.EmptyContent)
            } else {
                emit(GetFriendsState.Success(friends))
            }
        } catch (e: RetrofitException) {
            emit(GetFriendsState.Error(RetrofitExceptionHandler.getErrorMessage(e)))
        } catch (e: ConnectException) {
            emit(GetFriendsState.Error("Ошибка подключения к интернету"))
        } catch (e: Exception) {
            Log.d("OkHttp ex", e.message.toString())
            emit(GetFriendsState.Error(e.message.toString()))
        }
    }
}