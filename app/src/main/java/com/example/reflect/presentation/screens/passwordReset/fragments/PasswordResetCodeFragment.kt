package com.example.reflect.presentation.screens.passwordReset.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.reflect.R
import com.example.reflect.databinding.FragmentPasswordResetCodeBinding
import com.example.reflect.presentation.screens.passwordReset.viewmodel.ViewModelPasswordReset
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PasswordResetCodeFragment : Fragment() {

    private var _binding: FragmentPasswordResetCodeBinding? = null
    private val binding get() = _binding!!

    private val vm: ViewModelPasswordReset by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPasswordResetCodeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        if (!vm.isTimerStarted()) startTimer()
        startTimer()
        binding.resetPasswordCodeTitle.text = getString(R.string.resetPasswordCodeTitle, vm.email.value)
        bindViewModelAndPinCodeTextField()
        setOnClickLogic()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun bindViewModelAndPinCodeTextField() {
        with(binding) {
            resetPasswordCodePin.setText(vm.pinCode.value)
            resetPasswordCodePin.doAfterTextChanged { value ->
                vm.updatePinCode(value.toString())
                changeErrorStates(pinCodeError = false)
                resetPasswordSendCodeButton.isEnabled = value?.length == 4
            }
        }
    }

    private fun setOnClickLogic() {
        with(binding) {
            resetPasswordSendCodeButton.setOnClickListener {
                if(!vm.isCodeValid()) {
                    changeErrorStates(errorMessage = getText(R.string.resetPasswordCodePinErrorMessage).toString())
                } else {
                    vm.cancelTimer()
                    findNavController().navigate(R.id.action_passwordResetCodeFragment_to_passwordResetNewPasswordFragment)
                }
            }

            resetPasswordCodePin.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (vm.pinCode.value?.length == 4) {
                        resetPasswordSendCodeButton.performClick()
                    }
                }
                true
            }

            resetPasswordSendCodeAgainTV.setOnClickListener {
                vm.generatePinCode()
                Toast.makeText(context, vm.sendedPinCode.value, Toast.LENGTH_SHORT).show()
                startTimer()
            }
        }
    }

    private fun changeErrorStates(
        pinCodeError: Boolean = true,
        errorMessage: String = ""
    ) {
        with(binding) {
            resetPasswordCodePin.setLineColor(
                if (pinCodeError) ContextCompat.getColor(requireContext(), R.color.error)
                else ContextCompat.getColor(requireContext(), R.color.tertiary)
            )
            resetPasswordCodePinErrorMessage.text = errorMessage
        }
    }

    private fun startTimer(duration: Int = 30) {
        with(binding) {
            vm.startTimer(duration,
                onTick = {
                    resetPasswordSendCodeAgainTV.text = getString(R.string.resetPasswordSendCodeAgain, "0:%02d".format(it))
                    resetPasswordSendCodeAgainTV.isClickable = false
                    resetPasswordSendCodeAgainTV.setTextColor(ContextCompat.getColor(requireContext(), R.color.outline))
                },
                onFinish = {
                    resetPasswordSendCodeAgainTV.text = getString(R.string.resetPasswordSendCodeAgainWithoutTimer)
                    resetPasswordSendCodeAgainTV.isClickable = true
                    resetPasswordSendCodeAgainTV.setTextColor(ContextCompat.getColor(requireContext(), R.color.onSurfaceVariant))
                })
        }
    }
}