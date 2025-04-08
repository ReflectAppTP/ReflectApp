package com.example.reflect.presentation.screens.passwordReset.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.example.reflect.R
import com.example.reflect.common.Utils
import com.example.reflect.databinding.FragmentPasswordResetNewPasswordBinding
import com.example.reflect.presentation.screens.passwordReset.viewmodel.ViewModelPasswordReset
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PasswordResetNewPasswordFragment : Fragment() {

    private var _binding: FragmentPasswordResetNewPasswordBinding? = null
    private val binding get() = _binding!!

    private val vm: ViewModelPasswordReset by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPasswordResetNewPasswordBinding.inflate(inflater, container, false)

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
        with (binding) {
            resetPasswordNewPasswordEditTextField.setText(vm.newPassword.value)
            resetPasswordNewPasswordEditTextField.doAfterTextChanged { value ->
                vm.updateNewPassword(value.toString())
                changeErrorState(passwordError = false)
                resetPasswordChangePasswordButton.isEnabled = !areFieldsEmpty()
                resetPasswordNewPasswordEditText.isCounterEnabled =
                    value.toString().length >= resources.getInteger(R.integer.counterPasswordLength) - resources.getInteger(R.integer.characterLimit)
            }

            resetPasswordNewPasswordConfirmationEditTextField.setText(vm.newPasswordConfirmation.value)
            resetPasswordNewPasswordConfirmationEditTextField.doAfterTextChanged { value ->
                vm.updateNewPasswordConfirmation(value.toString())
                changeErrorState(passwordError = false)
                resetPasswordChangePasswordButton.isEnabled = !areFieldsEmpty()
                resetPasswordNewPasswordConfirmationEditText.isCounterEnabled =
                    value.toString().length >= resources.getInteger(R.integer.counterPasswordLength) - resources.getInteger(R.integer.characterLimit)
            }
        }
    }

    private fun setOnClickLogic() {
        with(binding) {
            val navController = Navigation.findNavController(requireActivity(), R.id.mainFragmentContainer)

            resetPasswordChangePasswordButton.setOnClickListener {
                if (vm.newPassword.value == vm.newPasswordConfirmation.value) {
                    navController.navigate(R.id.action_passwordResetNewPasswordFragment_to_mainFragment)
                } else {
                    changeErrorState(errorMessage = getString(R.string.inequalityFieldsErrorMessage))
                }
            }

            resetPasswordNewPasswordConfirmationEditTextField.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    resetPasswordChangePasswordButton.performClick()
                }
                true
            }
        }
    }

    private fun areFieldsEmpty() = with(binding) {
        Utils.isEditTextEmpty(resetPasswordNewPasswordEditTextField) ||
                Utils.isEditTextEmpty(resetPasswordNewPasswordConfirmationEditTextField)
    }

    private fun changeErrorState(
        passwordError: Boolean = true,
        errorMessage: String = ""
    ) {
        with (binding) {
            resetPasswordNewPasswordEditText.error = if (passwordError) " " else ""
            resetPasswordNewPasswordConfirmationEditText.error = if (passwordError) " " else ""
            resetPasswordNewPasswordErrorMessage.text = errorMessage
        }
    }

}