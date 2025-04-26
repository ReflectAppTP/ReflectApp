package com.example.reflect.presentation.screens.records.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.reflect.domain.model.RecordModel
import com.example.reflect.domain.model.TagModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class ViewModelRecords @Inject constructor() : ViewModel() {
    // Надо это поле делать private или нет?
    private val calendar = Calendar.getInstance()

    private var _selectedDate = MutableLiveData(calendar.time)
    val selectedDate: LiveData<Date> get() = _selectedDate

    private var _records = MutableLiveData(emptyList<RecordModel>())
    val records: LiveData<List<RecordModel>> get() = _records

    private fun fetchRecords(date: Date){
        // TODO: impl
        _records.value = mutableListOf(
            RecordModel(1,1, null, null, null),
            RecordModel(2,3, null, null, "Жесткий понос"),
            RecordModel(3,4, listOf(
                TagModel(1, "Удивленно", "\uD83D\uDE2E"),
                TagModel(2, "Спокойно", "\uD83D\uDE0C"),
                ), null, "Жесткий понос прошёл"),
            RecordModel(4,6, listOf(
                TagModel(3, "Погода", "\u2600"),
                TagModel(4, "Соцсети", "\uD83D\uDCF1"),
            ),
                listOf(
                TagModel(1, "Удивленно", "\uD83D\uDE2E"),
                TagModel(2, "Спокойно", "\uD83D\uDE0C"),
                TagModel(7, "Окрыленно", "\uD83E\uDD29"),
                TagModel(8, "Воодушевленно", "\uD83D\uDE0D"),
            ), "Бахнул кофе и прям так легко на душе стало"),
            RecordModel(5,8, listOf(
                TagModel(3, "Погода", "\u2600"),
                TagModel(4, "Соцсети", "\uD83D\uDCF1"),
                TagModel(7, "Друзья", "\uD83D\uDC65"),
                TagModel(8, "Учеба", "\uD83D\uDCDA"),
                TagModel(9, "Покупки", "\uD83D\uDED2"),
            ),
                listOf(
                    TagModel(1, "Удивленно", "\uD83D\uDE2E"),
                    TagModel(2, "Спокойно", "\uD83D\uDE0C"),
                    TagModel(7, "Окрыленно", "\uD83E\uDD29"),
                    TagModel(8, "Воодушевленно", "\uD83D\uDE0D"),
                    TagModel(15, "Расстроенно", "\uD83D\uDE1E"),
                    TagModel(16, "Скучно", "\uD83D\uDE14"),
                    TagModel(17, "Тревожно", "\uD83E\uDD2F"),
                    TagModel(18, "Отвратительно", "\uD83D\uDE21")
                ), "Сладко поспал"),
            RecordModel(6, 10, null,
                listOf(
                    TagModel(1, "Удивленно", "\uD83D\uDE2E"),
                    TagModel(2, "Спокойно", "\uD83D\uDE0C"),
                    TagModel(7, "Окрыленно", "\uD83E\uDD29"),
                    TagModel(8, "Воодушевленно", "\uD83D\uDE0D"),
                    TagModel(15, "Расстроенно", "\uD83D\uDE1E"),
                    TagModel(16, "Скучно", "\uD83D\uDE14"),
                    TagModel(17, "Тревожно", "\uD83E\uDD2F"),
                    TagModel(18, "Отвратительно", "\uD83D\uDE21")
                ), "Сладко поспал и бахнул пивка"),
        )
    }

    fun updateSelectedDate(date: Date) {
        _selectedDate.value = date
        fetchRecords(date)
    }
}