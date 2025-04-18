package com.example.reflect.presentation.screens.addState.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.reflect.domain.model.TagModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddStateViewModel @Inject constructor() : ViewModel() {

    private var _firstTags = MutableLiveData<MutableList<TagModel>>(mutableListOf())
    val firstTags: LiveData<MutableList<TagModel>> get() = _firstTags

    private var _secondTags = MutableLiveData<MutableList<TagModel>>(mutableListOf())
    val secondTags: LiveData<MutableList<TagModel>> get() = _secondTags

    private var _selectedFirstTags = MutableLiveData<MutableList<Int>>(mutableListOf())
    val selectedFirtsTags: LiveData<MutableList<Int>> get() = _selectedFirstTags

    private var _selectedSecondTags = MutableLiveData<MutableList<Int>>(mutableListOf())
    val selectedSecondTags: LiveData<MutableList<Int>> get() = _selectedSecondTags

    fun fetchFirstTags() {
        _firstTags.value = mutableListOf(
            TagModel(1, "Партнер", "\uD83D\uDC6A"),
            TagModel(2, "Спорт", "\uD83C\uDFC3"),
            TagModel(3, "Погода", "\u2600"),
            TagModel(4, "Соцсети", "\uD83D\uDCF1"),
            TagModel(5, "Игры", "\uD83C\uDFAE"),
            TagModel(6, "Работа", "\uD83D\uDCCA"),
            TagModel(7, "Друзья", "\uD83D\uDC65"),
            TagModel(8, "Учеба", "\uD83D\uDCDA"),
            TagModel(9, "Покупки", "\uD83D\uDED2"),
            TagModel(10, "Музыка", "\uD83C\uDFB5"),
            TagModel(11, "Уборка", "\uD83E\uDDF9"),
            TagModel(12, "Отдых", "\uD83D\uDE34"),
            TagModel(13, "Питомец", "\uD83D\uDC36"),
            TagModel(14, "Семья", "\uD83D\uDC6A"),
            TagModel(15, "Здоровье", "\uD83D\uDC8A"),
            TagModel(16, "Кино и TV", "\uD83C\uDFA5"),
            TagModel(17, "Еда", "\uD83C\uDF54"),
            TagModel(18, "Финансы", "\uD83D\uDCB0"),
            TagModel(19, "Хобби", "\uD83C\uDFA8"),
            TagModel(20, "Сон", "\uD83D\uDE34"),
            TagModel(21, "Природа", "\uD83C\uDF32"),
            TagModel(22, "Путешествия", "\uD83D\uDEEB"),
        )
    }

    fun fetchSecondTags(): List<TagModel>? {
        // TODO: impl
        return null
    }

    fun addTagIdToFirstList(id: Int) {
        _selectedFirstTags.value?.add(id)
    }


    fun clearData() {
        _firstTags.value = mutableListOf()
        _secondTags.value = mutableListOf()
        _selectedFirstTags.value = mutableListOf()
        _selectedSecondTags.value = mutableListOf()
    }
}