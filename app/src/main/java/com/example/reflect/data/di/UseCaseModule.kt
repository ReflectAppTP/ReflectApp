package com.example.reflect.data.di

import com.example.reflect.data.repository.RegistrationRepositoryImpl
import com.example.reflect.domain.usecase.RegistrationUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {

    @Binds
    abstract fun bindRegistrationUseCase(
        registrationRepositoryImpl: RegistrationRepositoryImpl
    ): RegistrationUseCase
}