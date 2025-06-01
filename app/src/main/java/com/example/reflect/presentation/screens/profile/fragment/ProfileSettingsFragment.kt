package com.example.reflect.presentation.screens.profile.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.reflect.R
import com.example.reflect.common.Utils
import com.example.reflect.common.prefs.AccountPrefs
import com.example.reflect.databinding.FragmentProfileSettingsBinding
import com.example.reflect.presentation.screens.profile.SettingsProfileIntent
import com.example.reflect.presentation.screens.profile.viewmodel.ViewModelProfileSettings
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class ProfileSettingsFragment : Fragment() {

    private var _binding: FragmentProfileSettingsBinding? = null
    private val binding get() = _binding!!

    private val vm: ViewModelProfileSettings by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentProfileSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with (binding) {
            setValueToFields()
            bindViewModelAndTextFields()

            root.setOnClickListener { clickedView ->
                if (clickedView !is TextInputEditText) {
                    hideKeyboard()
                }
            }

            fragmentProfileSaveChanges.setOnClickListener {
                hideKeyboard()
                if (areFieldsEmpty()) {
                    changeErrorStates(errorMessage = getText(R.string.emptyFieldsErrorMessage).toString())
                    return@setOnClickListener
                }

                val oldPassword = fragmentProfilePasswordEditTextField.text.toString()
                val newPassword = fragmentProfilePasswordConfirmationEditTextField.text.toString()
                val isPasswordValid = vm.isPasswordMoreThanSixSymbols()

                when {
                    oldPassword == newPassword && isPasswordValid -> {
                        lifecycleScope.launch {
                            vm.userIntent.send(SettingsProfileIntent.SaveChanges)
                        }
                    }
                    !isPasswordValid -> {
                        changeErrorStates(
                            loginError = false,
                            errorMessage = getText(R.string.passwordInSixSymbolsErrorMessage).toString()
                        )
                    }
                    else -> {
                        changeErrorStates(
                            loginError = false,
                            errorMessage = getText(R.string.inequalityFieldsErrorMessage).toString()
                        )
                    }
                }
            }

            fragmentProfileSettingsBackArrow.setOnClickListener {
                findNavController().popBackStack()
            }

            fragmentProfilePasswordConfirmationEditTextField.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    fragmentProfileSaveChanges.performClick()
                }
                true
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setValueToFields() {
        val user = AccountPrefs.getUser(requireContext())
        vm.updateVisibility("Никому")
        vm.updateLogin(user.username)
        with (binding) {
            fragmentProfileLoginEditTextField.setText(vm.login.value)
            fragmentProfileVisibilityField.setText(vm.visibility.value, false)
        }
    }

    private fun bindViewModelAndTextFields() {
        with (binding) {
            fragmentProfileLoginEditTextField.setText(vm.login.value)
            fragmentProfileLoginEditTextField.doAfterTextChanged { value ->
                vm.updateLogin(value.toString())
                changeErrorStates(
                    loginError = false,
                    oldPassword = false,
                    newPassword = false
                )
                fragmentProfileLoginEditText.isCounterEnabled =
                    value.toString().length >= resources.getInteger(R.integer.counterLoginLength) - resources.getInteger(R.integer.characterLimit)
            }

            fragmentProfilePasswordEditTextField.setText(vm.oldPassword.value)
            fragmentProfilePasswordEditTextField.doAfterTextChanged { value ->
                vm.updateOldPassword(value.toString())
                changeErrorStates(
                    loginError = false,
                    oldPassword = false,
                    newPassword = false
                )
                fragmentProfilePasswordEditText.isCounterEnabled =
                    value.toString().length >= resources.getInteger(R.integer.counterLoginLength) - resources.getInteger(R.integer.characterLimit)
            }

            fragmentProfilePasswordConfirmationEditTextField.setText(vm.newPassword.value)
            fragmentProfilePasswordConfirmationEditTextField.doAfterTextChanged { value ->
                vm.updateNewPassword(value.toString())
                changeErrorStates(
                    loginError = false,
                    oldPassword = false,
                    newPassword = false
                )
                fragmentProfilePasswordConfirmationEditText.isCounterEnabled =
                    value.toString().length >= resources.getInteger(R.integer.counterLoginLength) - resources.getInteger(R.integer.characterLimit)
            }
        }
    }

    private fun areFieldsEmpty() = with (binding) {
        Utils.isEditTextEmpty(fragmentProfileLoginEditTextField) ||
                Utils.isEditTextEmpty(fragmentProfilePasswordEditTextField) ||
                Utils.isEditTextEmpty(fragmentProfilePasswordConfirmationEditTextField)
    }

    private fun changeErrorStates(
        loginError: Boolean = true,
        oldPassword: Boolean = true,
        newPassword: Boolean = true,
        errorMessage: String = ""
    ) {
        with (binding) {
            fragmentProfileLoginEditText.error = if (loginError) " " else ""
            fragmentProfilePasswordEditText.error = if (oldPassword) " " else ""
            fragmentProfilePasswordConfirmationEditText.error = if (newPassword) " " else ""
            fragmentProfilePasswordErrorMessage.text = errorMessage
        }
    }

    private fun hideKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        with (binding) {
            fragmentProfileLoginEditTextField.clearFocus()
            fragmentProfilePasswordEditTextField.clearFocus()
            fragmentProfilePasswordConfirmationEditTextField.clearFocus()
        }
        imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }
}