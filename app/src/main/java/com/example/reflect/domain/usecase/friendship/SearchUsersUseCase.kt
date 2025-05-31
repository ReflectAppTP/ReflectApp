package com.example.reflect.domain.usecase.friendship

import android.util.Log
import com.example.reflect.common.RetrofitException
import com.example.reflect.common.RetrofitExceptionHandler
import com.example.reflect.domain.repository.friendship.SearchUsersRepository
import com.example.reflect.presentation.screens.friends.SearchFriendsState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.net.ConnectException
import javax.inject.Inject

class SearchUsersUseCase @Inject constructor(
    private val searchUsersRepository: SearchUsersRepository
) {
    suspend operator fun invoke(username: String): Flow<SearchFriendsState> = flow {
        emit(SearchFriendsState.Loading)
        try {
            val users = searchUsersRepository.searchUsers(username)
            if (users.isEmpty()) {
                emit(SearchFriendsState.EmptyContent)
            } else {
                emit(SearchFriendsState.Success(users))
            }
        } catch (e: RetrofitException) {
            emit(SearchFriendsState.Error(RetrofitExceptionHandler.getErrorMessage(e)))
        } catch (e: ConnectException) {
            emit(SearchFriendsState.Error("Ошибка подключения к интернету"))
        } catch (e: Exception) {
            Log.d("OkHttp ex", e.message.toString())
            emit(SearchFriendsState.Error(e.message.toString()))
        }
    }
}