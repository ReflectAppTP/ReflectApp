package com.example.reflect.presentation.screens.addState.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.reflect.domain.model.TagModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddStateViewModel @Inject constructor() : ViewModel() {

    private var _emotionalState = MutableLiveData(5f)
    val emotionalState: LiveData<Float> get() = _emotionalState

    private var _firstTags = MutableLiveData<MutableList<TagModel>>(mutableListOf())
    val firstTags: LiveData<MutableList<TagModel>> get() = _firstTags

    private var _secondTags = MutableLiveData<MutableList<TagModel>>(mutableListOf())
    val secondTags: LiveData<MutableList<TagModel>> get() = _secondTags

    private var _emotionalDescription = MutableLiveData("")
    val emotionalDescription: LiveData<String> get() = _emotionalDescription

    private var _selectedFirstTags = MutableLiveData<MutableList<Int>>(mutableListOf())
    val selectedFirstTags: LiveData<MutableList<Int>> get() = _selectedFirstTags

    private var _selectedSecondTags = MutableLiveData<MutableList<Int>>(mutableListOf())
    val selectedSecondTags: LiveData<MutableList<Int>> get() = _selectedSecondTags

    fun fetchFirstTags() {
        // TODO: Сделать по-человечески
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

    fun fetchSecondTags() {
        // TODO: impl
        _secondTags.value = mutableListOf(
            TagModel(1, "Удивленно", "\uD83D\uDE2E"),
            TagModel(2, "Спокойно", "\uD83D\uDE0C"),
            TagModel(3, "Удовлетворенно", "\uD83D\uDE0A"),
            TagModel(4, "Счастливо", "\uD83D\uDE01"),
            TagModel(5, "Расслабленно", "\uD83D\uDE34"),
            TagModel(6, "Безмятежно", "\uD83D\uDE07"),
            TagModel(7, "Окрыленно", "\uD83E\uDD29"),
            TagModel(8, "Воодушевленно", "\uD83D\uDE0D"),
            TagModel(9, "Устало", "\uD83D\uDE13"),
            TagModel(10, "Грустно", "\uD83D\uDE22"),
            TagModel(11, "Напряженно", "\uD83D\uDE15"),
            TagModel(12, "Депрессивно", "\uD83D\uDE2D"),
            TagModel(13, "В стрессе", "\uD83E\uDD2C"),
            TagModel(14, "Нервно", "\uD83E\uDD75"),
            TagModel(15, "Расстроенно", "\uD83D\uDE1E"),
            TagModel(16, "Скучно", "\uD83D\uDE14"),
            TagModel(17, "Тревожно", "\uD83E\uDD2F"),
            TagModel(18, "Отвратительно", "\uD83D\uDE21")
        )
    }

    fun updateEmotionalState(state: Float) {
        _emotionalState.value = state
    }

    fun updateEmotionalDescription(description: String) {
        _emotionalDescription.value = description
    }

    fun addTagIdToFirstList(id: Int) {
        _selectedFirstTags.value?.add(id)
    }

    fun addTagIdToSecondList(id: Int) {
        _selectedSecondTags.value?.add(id)
    }


    fun clearData() {
        _emotionalState.value = 5f
        _firstTags.value = mutableListOf()
        _secondTags.value = mutableListOf()
        _emotionalDescription.value = ""

        _selectedFirstTags.value = mutableListOf()
        _selectedSecondTags.value = mutableListOf()
    }
}