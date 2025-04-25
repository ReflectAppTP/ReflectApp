package com.example.reflect.presentation.screens.profile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ViewModelProfile @Inject constructor(): ViewModel() {
    private var _login = MutableLiveData("")
    val login: LiveData<String> get() = _login

}