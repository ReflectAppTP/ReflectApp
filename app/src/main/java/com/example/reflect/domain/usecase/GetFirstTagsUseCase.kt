package com.example.reflect.domain.usecase

import com.example.reflect.common.RetrofitException
import com.example.reflect.common.RetrofitExceptionHandler
import com.example.reflect.domain.repository.GetFirstTagsRepository
import com.example.reflect.presentation.screens.addState.TagsState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.net.ConnectException
import javax.inject.Inject

class GetFirstTagsUseCase @Inject constructor(
    private val getFirstTagsRepository: GetFirstTagsRepository
) {
    suspend operator fun invoke(): Flow<TagsState> = flow {
        try {
            val tags = getFirstTagsRepository.getFirstTags()
            emit(TagsState.Success(tags))
        } catch (e: RetrofitException) {
            emit(TagsState.Error(RetrofitExceptionHandler.getErrorMessage(e)))
        } catch (e: ConnectException) {
            emit(TagsState.Error("Ошибка подключения к интернету"))
        } catch (e: Exception) {
            emit(TagsState.Error("Какая то ошибка"))
        }
    }
}