package com.example.reflect.domain.usecase.friendship

import android.util.Log
import com.example.reflect.common.RetrofitException
import com.example.reflect.common.RetrofitExceptionHandler
import com.example.reflect.domain.repository.friendship.ChangePremiumRepository
import com.example.reflect.presentation.screens.premium.UpdatePremiumState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.net.ConnectException
import javax.inject.Inject

class ChangePremiumUseCase @Inject constructor(
    private val changePremiumRepository: ChangePremiumRepository
) {
    suspend operator fun invoke(isPremium: Boolean): Flow<UpdatePremiumState> = flow {
        emit(UpdatePremiumState.Loading)
        try {
            val response = changePremiumRepository.updatePremium(isPremium)
            emit(UpdatePremiumState.Success)
        } catch (e: RetrofitException) {
            emit(UpdatePremiumState.Error(RetrofitExceptionHandler.getErrorMessage(e)))
        } catch (e: ConnectException) {
            emit(UpdatePremiumState.Error("Ошибка подключения к интернету"))
        } catch (e: Exception) {
            Log.d("OkHttp ex", e.message.toString())
            emit(UpdatePremiumState.Error(e.message.toString()))
        }
    }
}