package com.example.reflect.presentation.screens.registration.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.reflect.R
import com.example.reflect.common.Utils
import com.example.reflect.databinding.FragmentRegistrationBinding
import com.example.reflect.presentation.screens.registration.viewmodel.ViewModelRegistration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegistrationFragment : Fragment() {

    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!

    private val vm: ViewModelRegistration by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindViewModelAndTextFields()
        setOnClickLogic()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun bindViewModelAndTextFields() {
        with(binding) {
            registrationLoginEditTextField.setText(vm.login.value)
            registrationLoginEditTextField.doAfterTextChanged { value ->
                vm.updateLogin(value.toString())
                changeErrorStates(
                    loginError = false,
                    emailError = false,
                    passwordError = false,
                    passwordConfirmationError = false
                )
                registrationLoginEditText.isCounterEnabled =
                    value.toString().length >= resources.getInteger(R.integer.counterLoginLength) - resources.getInteger(R.integer.characterLimit)
            }

            registrationEmailEditTextField.setText(vm.email.value)
            registrationEmailEditTextField.doAfterTextChanged { value ->
                vm.updateEmail(value.toString())
                changeErrorStates(
                    loginError = false,
                    emailError = false,
                    passwordError = false,
                    passwordConfirmationError = false
                )
                registrationEmailEditText.isCounterEnabled =
                    value.toString().length >= resources.getInteger(R.integer.counterEmailLength) - resources.getInteger(R.integer.characterLimit)
            }

            registrationPasswordEditTextField.setText(vm.password.value)
            registrationPasswordEditTextField.doAfterTextChanged { value ->
                vm.updatePassword(value.toString())
                changeErrorStates(
                    loginError = false,
                    emailError = false,
                    passwordError = false,
                    passwordConfirmationError = false
                )
                registrationPasswordEditText.isCounterEnabled =
                    value.toString().length >= resources.getInteger(R.integer.counterPasswordLength) - resources.getInteger(R.integer.characterLimit)
            }

            registrationPasswordConfirmationEditTextField.setText(vm.passwordConfirmation.value)
            registrationPasswordConfirmationEditTextField.doAfterTextChanged { value ->
                vm.updatePasswordConfirmation(value.toString())
                changeErrorStates(
                    loginError = false,
                    emailError = false,
                    passwordError = false,
                    passwordConfirmationError = false
                )
                registrationPasswordConfirmationEditText.isCounterEnabled =
                    value.toString().length >= resources.getInteger(R.integer.counterPasswordLength) - resources.getInteger(R.integer.characterLimit)

            }

        }
    }

    private fun setOnClickLogic() {
        with(binding) {
            registrationButton.setOnClickListener {
                if (areFieldsEmpty()) {
                    changeErrorStates(errorMessage = getText(R.string.emptyFieldsErrorMessage).toString())
                    return@setOnClickListener
                }

                if (!Utils.isEmailValid(vm.email.value!!)) {
                    changeErrorStates(
                        loginError = false,
                        passwordError = false,
                        passwordConfirmationError = false,
                        errorMessage = getText(R.string.incorrectEmailErrorMessage).toString()
                    )
                    return@setOnClickListener
                }

                val password = registrationPasswordEditTextField.text.toString()
                val passwordConfirmation = registrationPasswordConfirmationEditTextField.text.toString()
                val isPasswordValid = vm.isPasswordMoreThanSixSymbols()

                when {
                    password == passwordConfirmation && isPasswordValid -> {
                        findNavController().navigate(R.id.action_registrationFragment_to_mainFragment)
                    }
                    !isPasswordValid -> {
                        changeErrorStates(
                            loginError = false,
                            emailError = false,
                            errorMessage = getText(R.string.passwordInSixSymbolsErrorMessage).toString()
                        )
                    }
                    else -> {
                        changeErrorStates(
                            loginError = false,
                            emailError = false,
                            errorMessage = getText(R.string.inequalityFieldsErrorMessage).toString()
                        )
                    }
                }
            }


            registrationPasswordConfirmationEditTextField.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    registrationButton.performClick()
                }
                true
            }

            registrationBackArrow.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun areFieldsEmpty() = with(binding) {
        Utils.isEditTextEmpty(registrationLoginEditTextField) ||
                Utils.isEditTextEmpty(registrationEmailEditTextField) ||
                Utils.isEditTextEmpty(registrationPasswordEditTextField) ||
                Utils.isEditTextEmpty(registrationPasswordConfirmationEditTextField)
    }


    private fun changeErrorStates(
        loginError: Boolean = true,
        emailError: Boolean = true,
        passwordError: Boolean = true,
        passwordConfirmationError: Boolean = true,
        errorMessage: String = ""
    ) {
        with(binding) {
            registrationLoginEditText.error = if (loginError) " " else ""
            registrationEmailEditText.error = if (emailError) " " else ""
            registrationPasswordEditText.error = if (passwordError) " " else ""
            registrationPasswordConfirmationEditText.error = if (passwordConfirmationError) " " else ""
            registrationErrorMessage.text = errorMessage
        }

    }
}



