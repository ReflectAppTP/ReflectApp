package com.example.reflect.domain.usecase

import com.example.reflect.domain.repository.RegistrationRepository
import com.example.reflect.presentation.screens.registration.RegistrationState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RegistrationUseCase @Inject constructor(
    private val registrationRepository: RegistrationRepository
) {
    suspend operator fun invoke(
        username: String,
        email: String,
        password: String
    ): Flow<RegistrationState> = flow {
        emit(RegistrationState.Loading)
        try {
            val user = registrationRepository.register(username, email, password)
            emit(RegistrationState.Success(user))
        } catch (e: Exception) {
            emit(RegistrationState.Error(e.message.toString()))
        }
    }
}