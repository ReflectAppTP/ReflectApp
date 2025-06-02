package com.example.reflect.data.di

import com.example.reflect.data.remote.data.RetrofitRemoteData
import com.example.reflect.data.repository.ai.GetAIMessageRepositoryImpl
import com.example.reflect.data.repository.ai.ResetAIContextRepositoryImpl
import com.example.reflect.data.repository.ai.SendAIMessageRepositoryImpl
import com.example.reflect.data.repository.state.AddStateRepositoryImpl
import com.example.reflect.data.repository.state.DeleteStateRepositoryImpl
import com.example.reflect.data.repository.state.EditStateRepositoryImpl
import com.example.reflect.data.repository.state.GetFirstTagsRepositoryImpl
import com.example.reflect.data.repository.auth.GetProfileRepositoryImpl
import com.example.reflect.data.repository.state.GetSecondTagsRepositoryImpl
import com.example.reflect.data.repository.state.GetStatesRepositoryImpl
import com.example.reflect.data.repository.auth.LoginRepositoryImpl
import com.example.reflect.data.repository.auth.RefreshRepositoryImpl
import com.example.reflect.data.repository.auth.RegistrationRepositoryImpl
import com.example.reflect.data.repository.friendship.GetFriendsListRepositoryImpl
import com.example.reflect.data.repository.friendship.GetUserByIdRepositoryImpl
import com.example.reflect.data.repository.friendship.SearchUsersRepositoryImpl
import com.example.reflect.data.repository.statistic.GetMonthlyAverageRepositoryImpl
import com.example.reflect.data.repository.statistic.GetStateFrequencyRepositoryImpl
import com.example.reflect.data.repository.statistic.GetStatisticEmotionalTagsRepositoryImpl
import com.example.reflect.data.repository.statistic.GetStatisticTagsRepositoryImpl
import com.example.reflect.data.repository.statistic.GetWeeklyAverageRepositoryImpl
import com.example.reflect.data.repository.statistic.GetYearlyAverageRepositoryImpl
import com.example.reflect.domain.repository.ai.GetAIMessageRepository
import com.example.reflect.domain.repository.ai.ResetAIContextRepository
import com.example.reflect.domain.repository.ai.SendAIMessageRepository
import com.example.reflect.domain.repository.state.AddStateRepository
import com.example.reflect.domain.repository.state.DeleteStateRepository
import com.example.reflect.domain.repository.state.EditStateRepository
import com.example.reflect.domain.repository.state.GetFirstTagsRepository
import com.example.reflect.domain.repository.auth.GetProfileRepository
import com.example.reflect.domain.repository.state.GetSecondTagsRepository
import com.example.reflect.domain.repository.state.GetStatesRepository
import com.example.reflect.domain.repository.auth.LoginRepository
import com.example.reflect.domain.repository.auth.RefreshRepository
import com.example.reflect.domain.repository.auth.RegistrationRepository
import com.example.reflect.domain.repository.friendship.GetFriendsListRepository
import com.example.reflect.domain.repository.friendship.GetUserByIdRepository
import com.example.reflect.domain.repository.friendship.SearchUsersRepository
import com.example.reflect.domain.repository.statistic.GetMonthlyAverageRepository
import com.example.reflect.domain.repository.statistic.GetStateFrequencyRepository
import com.example.reflect.domain.repository.statistic.GetStatisticEmotionalTagsRepository
import com.example.reflect.domain.repository.statistic.GetStatisticTagsRepository
import com.example.reflect.domain.repository.statistic.GetWeeklyAverageRepository
import com.example.reflect.domain.repository.statistic.GetYearlyAverageRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideRegistrationRepository(
        remoteData: RetrofitRemoteData
    ): RegistrationRepository = RegistrationRepositoryImpl(remoteData)

    @Provides
    @Singleton
    fun provideLoginRepository(
        remoteData: RetrofitRemoteData
    ): LoginRepository = LoginRepositoryImpl(remoteData)

    @Provides
    @Singleton
    fun provideGetProfileRepository(
        remoteData: RetrofitRemoteData
    ): GetProfileRepository = GetProfileRepositoryImpl(remoteData)

    @Provides
    @Singleton
    fun provideRefreshRepository(
        remoteData: RetrofitRemoteData
    ): RefreshRepository = RefreshRepositoryImpl(remoteData)

    @Provides
    @Singleton
    fun provideGetFirstTagsRepository(
        remoteData: RetrofitRemoteData
    ): GetFirstTagsRepository = GetFirstTagsRepositoryImpl(remoteData)

    @Provides
    @Singleton
    fun provideGetSecondTagsRepository(
        remoteData: RetrofitRemoteData
    ): GetSecondTagsRepository = GetSecondTagsRepositoryImpl(remoteData)

    @Provides
    @Singleton
    fun provideAddStateRepository(
        remoteData: RetrofitRemoteData
    ): AddStateRepository = AddStateRepositoryImpl(remoteData)

    @Provides
    @Singleton
    fun provideGetStatesRepository(
        remoteData: RetrofitRemoteData
    ): GetStatesRepository = GetStatesRepositoryImpl(remoteData)

    @Provides
    @Singleton
    fun provideEditStateRepository(
        remoteData: RetrofitRemoteData
    ): EditStateRepository = EditStateRepositoryImpl(remoteData)

    @Provides
    @Singleton
    fun provideDeleteStateRepository(
        remoteData: RetrofitRemoteData
    ): DeleteStateRepository = DeleteStateRepositoryImpl(remoteData)

    @Provides
    @Singleton
    fun provideGetStateFrequency(
        remoteData: RetrofitRemoteData
    ): GetStateFrequencyRepository = GetStateFrequencyRepositoryImpl(remoteData)

    @Provides
    @Singleton
    fun provideGetStatisticTags(
        remoteData: RetrofitRemoteData
    ): GetStatisticTagsRepository = GetStatisticTagsRepositoryImpl(remoteData)

    @Provides
    @Singleton
    fun provideGetStatisticEmotionalTags(
        remoteData: RetrofitRemoteData
    ): GetStatisticEmotionalTagsRepository = GetStatisticEmotionalTagsRepositoryImpl(remoteData)

    @Provides
    @Singleton
    fun provideGetWeeklyAverage(
        remoteData: RetrofitRemoteData
    ): GetWeeklyAverageRepository = GetWeeklyAverageRepositoryImpl(remoteData)

    @Provides
    @Singleton
    fun provideGetMonthlyAverage(
        remoteData: RetrofitRemoteData
    ): GetMonthlyAverageRepository = GetMonthlyAverageRepositoryImpl(remoteData)

    @Provides
    @Singleton
    fun provideGetYearlyAverage(
        remoteData: RetrofitRemoteData
    ): GetYearlyAverageRepository = GetYearlyAverageRepositoryImpl(remoteData)

    @Provides
    @Singleton
    fun provideAISendMessage(
        remoteData: RetrofitRemoteData
    ): SendAIMessageRepository = SendAIMessageRepositoryImpl(remoteData)

    @Provides
    @Singleton
    fun provideResetContext(
        remoteData: RetrofitRemoteData
    ): ResetAIContextRepository = ResetAIContextRepositoryImpl(remoteData)

    @Provides
    @Singleton
    fun provideGetAIMessage(
        remoteData: RetrofitRemoteData
    ): GetAIMessageRepository = GetAIMessageRepositoryImpl(remoteData)

    @Provides
    @Singleton
    fun provideGetFriends(
        remoteData: RetrofitRemoteData
    ): GetFriendsListRepository = GetFriendsListRepositoryImpl(remoteData)

    @Provides
    @Singleton
    fun provideSearchUsers(
        remoteData: RetrofitRemoteData
    ): SearchUsersRepository = SearchUsersRepositoryImpl(remoteData)

    @Provides
    @Singleton
    fun provideGetUserById(
        remoteData: RetrofitRemoteData
    ): GetUserByIdRepository = GetUserByIdRepositoryImpl(remoteData)
}