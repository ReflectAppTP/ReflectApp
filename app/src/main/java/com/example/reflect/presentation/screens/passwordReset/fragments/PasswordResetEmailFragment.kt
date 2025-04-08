package com.example.reflect.presentation.screens.passwordReset.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.reflect.R
import com.example.reflect.databinding.FragmentPasswordResetEmailBinding
import com.example.reflect.presentation.screens.passwordReset.viewmodel.ViewModelPasswordReset
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PasswordResetEmailFragment : Fragment() {

    private var _binding: FragmentPasswordResetEmailBinding? = null
    private val binding get() = _binding!!

    private val vm: ViewModelPasswordReset by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPasswordResetEmailBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindViewModelAndTextField()
        setOnClickLogic()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun bindViewModelAndTextField() {
        with(binding) {
            resetPasswordEmailEditTextField.setText(vm.email.value)
            resetPasswordEmailEditTextField.doAfterTextChanged { value ->
                vm.updateEmail(value.toString())
                changeErrorState(emailError = false)
                resetPasswordSendCodeButton.isEnabled = !value.isNullOrEmpty()
                resetPasswordEmailEditText.isCounterEnabled =
                    value.toString().length >= resources.getInteger(R.integer.counterEmailLength) - resources.getInteger(R.integer.characterLimit)
            }
        }
    }

    private fun setOnClickLogic() {
        with(binding) {
            resetPasswordSendCodeButton.setOnClickListener {
                vm.generatePinCode()
                Toast.makeText(context, vm.sendedPinCode.value, Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_passwordResetEmailFragment_to_passwordResetCodeFragment)
            }

            resetPasswordEmailEditTextField.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    resetPasswordSendCodeButton.performClick()
                }
                true
            }
        }
    }

    private fun changeErrorState(
        emailError: Boolean = true,
        errorMessage: String = ""
    ) {
        with(binding) {
            resetPasswordEmailEditText.error = if (emailError) " " else ""
            resetPasswordEmailErrorMessage.text = errorMessage
        }
    }
}