package com.example.reflect.presentation.screens.passwordReset.viewmodel


import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelPasswordReset @Inject constructor() : ViewModel() {
    // TODO: remove unused fields
    private var _email = MutableStateFlow("")
    val email: StateFlow<String> get() = _email

    private var _pinCode = MutableStateFlow("")
    val pinCode: StateFlow<String> get() = _pinCode

    private var _sendedPinCode = MutableStateFlow("")
    val sendedPinCode: StateFlow<String> get() = _sendedPinCode

    private var _newPassword = MutableStateFlow("")
    val newPassword: StateFlow<String> get() = _newPassword

    private var _newPasswordConfirmation = MutableStateFlow("")
    val newPasswordConfirmation: StateFlow<String> get() = _newPasswordConfirmation

    private var _emailErrorState = MutableStateFlow(false)
    val emailErrorState: StateFlow<Boolean> get() = _emailErrorState

    private var _pinCodeErrorState = MutableStateFlow(false)
    val pinCodeErrorState: StateFlow<Boolean> get() = _pinCodeErrorState

    private var _newPasswordErrorState = MutableStateFlow(false)
    val newPasswordErrorState: StateFlow<Boolean> get() = _newPasswordErrorState

    private var _newPasswordConfirmationErrorState = MutableStateFlow(false)
    val newPasswordConfirmationErrorState: StateFlow<Boolean> get() = _newPasswordConfirmationErrorState

    private var timerJob: Job? = null

    fun updateEmail(value: String) {
        _email.value = value
    }

    fun updatePinCode(value: String) {
        _pinCode.value = value
    }

    fun updateNewPassword(value: String) {
        _newPassword.value = value
    }

    fun updateNewPasswordConfirmation(value: String) {
        _newPasswordConfirmation.value = value
    }

    fun generatePinCode() {
        val code = (0..9).shuffled().take(4).joinToString("")
        _sendedPinCode.value = code
    }

    fun changeErrorStates(
        emailError: Boolean = true,
        pinCodeError: Boolean = true,
        newPasswordError: Boolean = true,
        newPasswordConfirmationError: Boolean = true
    ) {
        _emailErrorState.value = emailError
        _pinCodeErrorState.value = pinCodeError
        _newPasswordErrorState.value = newPasswordError
        _newPasswordConfirmationErrorState.value = newPasswordConfirmationError
    }

    fun startTimer(duration: Int, onTick: (Int) -> Unit, onFinish: () -> Unit) {
        timerJob?.cancel()

        timerJob = CoroutineScope(Dispatchers.Main).launch {
            for (i in duration downTo 1) {
                onTick(i)
                delay(1000)
            }

            onFinish()
        }
    }

    fun cancelTimer() {
        timerJob?.cancel()
    }

    fun isTimerStarted() = timerJob?.isActive.let { false }

    fun isCodeValid(): Boolean = _sendedPinCode.value == _pinCode.value

    fun resetDataOnPopBackStack(withEmail: Boolean = false) {
        _newPassword.value = ""
        _newPasswordConfirmation.value = ""
        _pinCode.value = ""
        _sendedPinCode.value = ""
        if (withEmail) _email.value = ""
    }

    fun isPasswordMoreThanSixSymbols() = _newPassword.value!!.length >= 6 && _newPasswordConfirmation.value!!.length >= 6
}