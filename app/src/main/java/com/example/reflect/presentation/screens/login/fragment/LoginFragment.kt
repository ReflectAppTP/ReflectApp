package com.example.reflect.presentation.screens.login.fragment

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
import com.example.reflect.databinding.FragmentLoginBinding
import com.example.reflect.presentation.screens.login.viewmodel.ViewModelLogin

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val vm: ViewModelLogin by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

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
            emailLoginEditTextField.setText(vm.email.value)
            emailLoginEditTextField.doAfterTextChanged { value ->
                vm.updateEmail(value.toString())
                emailLoginEditText.isCounterEnabled =
                    value.toString().length >= resources.getInteger(R.integer.counterEmailLength) - resources.getInteger(R.integer.characterLimit)
            }

            passwordLoginEditTextField.setText(vm.password.value)
            passwordLoginEditTextField.doAfterTextChanged { value ->
                vm.updatePassword(value.toString())
                passwordLoginEditText.isCounterEnabled =
                    value.toString().length >= resources.getInteger(R.integer.counterPasswordLength) - resources.getInteger(R.integer.characterLimit)
            }
        }
    }

    private fun setOnClickLogic() {
        with(binding) {
            loginButton.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
            }

            passwordLoginEditTextField.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
                }
                true
            }

            loginRegistrationButton.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
            }
        }
    }
}