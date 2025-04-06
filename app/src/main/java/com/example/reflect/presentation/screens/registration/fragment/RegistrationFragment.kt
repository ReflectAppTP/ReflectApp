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
import com.example.reflect.databinding.FragmentRegistrationBinding
import com.example.reflect.presentation.screens.registration.viewmodel.ViewModelRegistration


class RegistrationFragment : Fragment() {

    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!

    private val vm: ViewModelRegistration by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)

        bindViewModelAndTextFields()
        setOnClickLogic()

        return binding.root
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
                registrationLoginEditText.isCounterEnabled =
                    value.toString().length >= resources.getInteger(R.integer.counterLoginLength) - resources.getInteger(R.integer.characterLimit)
            }

            registrationEmailEditTextField.setText(vm.email.value)
            registrationEmailEditTextField.doAfterTextChanged { value ->
                vm.updateEmail(value.toString())
                registrationEmailEditText.isCounterEnabled =
                    value.toString().length >= resources.getInteger(R.integer.counterEmailLength) - resources.getInteger(R.integer.characterLimit)
            }

            registrationPasswordEditTextField.setText(vm.password.value)
            registrationPasswordEditTextField.doAfterTextChanged { value ->
                vm.updatePassword(value.toString())
                registrationPasswordEditText.isCounterEnabled =
                    value.toString().length >= resources.getInteger(R.integer.counterPasswordLength) - resources.getInteger(R.integer.characterLimit)
            }

            registrationPasswordConfirmationEditTextField.setText(vm.passwordConfirmation.value)
            registrationPasswordConfirmationEditTextField.doAfterTextChanged { value ->
                vm.updatePasswordConfirmation(value.toString())
                registrationPasswordConfirmationEditText.isCounterEnabled =
                    value.toString().length >= resources.getInteger(R.integer.counterPasswordLength) - resources.getInteger(R.integer.characterLimit)

            }

        }
    }

    private fun setOnClickLogic() {
        with(binding) {
            registrationButton.setOnClickListener {
                findNavController().navigate(R.id.action_registrationFragment_to_mainFragment)
            }

            registrationPasswordConfirmationEditTextField.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    findNavController().navigate(R.id.action_registrationFragment_to_mainFragment)
                }
                true
            }

            registrationBackArrow.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }
}