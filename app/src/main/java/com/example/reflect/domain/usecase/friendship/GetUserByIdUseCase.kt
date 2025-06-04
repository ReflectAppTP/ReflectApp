package com.example.reflect.domain.usecase.friendship

import android.util.Log
import com.example.reflect.common.RetrofitException
import com.example.reflect.common.RetrofitExceptionHandler
import com.example.reflect.domain.repository.friendship.GetUserByIdRepository
import com.example.reflect.presentation.screens.profile.GetUserByIdState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.net.ConnectException
import javax.inject.Inject

class GetUserByIdUseCase @Inject constructor(
    private val getUserByIdRepository: GetUserByIdRepository
) {
    suspend operator fun invoke(id: Int): Flow<GetUserByIdState> = flow {
        emit(GetUserByIdState.Loading)
        try {
            val user = getUserByIdRepository.getUserById(id)
            emit(GetUserByIdState.Success(user))
        }catch (e: RetrofitException) {
            emit(GetUserByIdState.Error(RetrofitExceptionHandler.getErrorMessage(e)))
        } catch (e: ConnectException) {
            emit(GetUserByIdState.Error("Ошибка подключения к интернету"))
        } catch (e: Exception) {
            Log.d("OkHttp ex", e.message.toString())
            emit(GetUserByIdState.Error(e.message.toString()))
        }
    }
}