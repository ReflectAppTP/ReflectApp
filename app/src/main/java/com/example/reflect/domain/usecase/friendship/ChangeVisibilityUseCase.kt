package com.example.reflect.domain.usecase.friendship

import com.example.reflect.common.RetrofitException
import com.example.reflect.common.RetrofitExceptionHandler
import com.example.reflect.domain.repository.friendship.ChangeVisibilityRepository
import com.example.reflect.presentation.screens.profile.UpdateProfileState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.net.ConnectException
import javax.inject.Inject

class ChangeVisibilityUseCase @Inject constructor(
    private val changeVisibilityRepository: ChangeVisibilityRepository
) {
    suspend operator fun invoke(visibility: String): Flow<UpdateProfileState> = flow {
        try {
            val response = changeVisibilityRepository.updateVisibility(visibility)
            emit(UpdateProfileState.Success)
        } catch (e: RetrofitException) {
            emit(UpdateProfileState.Error(RetrofitExceptionHandler.getErrorMessage(e)))
        } catch (e: ConnectException) {
            emit(UpdateProfileState.Error("Ошибка подключения к интернету"))
        } catch (e: Exception) {
            emit(UpdateProfileState.Error("Какая то ошибка"))
        }
    }
}