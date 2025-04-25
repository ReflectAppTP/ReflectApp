package com.example.reflect.presentation.screens.login.fragment

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
import androidx.navigation.fragment.findNavController
import com.example.reflect.R
import com.example.reflect.common.AccountPrefs
import com.example.reflect.common.Utils
import com.example.reflect.databinding.FragmentLoginBinding
import com.example.reflect.presentation.screens.login.viewmodel.ViewModelLogin
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val vm: ViewModelLogin by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

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
            emailLoginEditTextField.setText(vm.email.value)
            emailLoginEditTextField.doAfterTextChanged { value ->
                vm.updateEmail(value.toString())
                changeErrorStates(emailError = false, passwordError = false)
                emailLoginEditText.isCounterEnabled =
                    value.toString().length >= resources.getInteger(R.integer.counterEmailLength) - resources.getInteger(R.integer.characterLimit)
            }

            passwordLoginEditTextField.setText(vm.password.value)
            passwordLoginEditTextField.doAfterTextChanged { value ->
                vm.updatePassword(value.toString())
                changeErrorStates(emailError = false, passwordError = false)
                passwordLoginEditText.isCounterEnabled =
                    value.toString().length >= resources.getInteger(R.integer.counterPasswordLength) - resources.getInteger(R.integer.characterLimit)
            }
        }
    }

    private fun setOnClickLogic() {
        with(binding) {
            loginButton.setOnClickListener {
                if (areFieldsEmpty()) {
                    changeErrorStates(errorMessage = getText(R.string.emptyFieldsErrorMessage).toString())
                } else {
                    // TODO: добавить бизнес логики (когда Ромчик подоит корову)
                    AccountPrefs.saveAuthState(requireContext(), true, "Надо получить токен от Ромы", userLogin = "Зареганый профиль")
                    findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
                }
            }

            root.setOnClickListener { clickedView ->
                if (clickedView !is TextInputEditText) {
                    hideKeyboard()
                }
            }

            passwordLoginEditTextField.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginButton.performClick()
                }
                true
            }

            loginLikeGuestButton.setOnClickListener {
                AccountPrefs.saveAuthState(requireContext(), false, "Наверно ещё один токен от Ромчика", true, "Супер гость")
                findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
            }

            loginRegistrationButton.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
            }

            loginForgotPasswordButton.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_resetPasswordFragment)
            }
        }
    }

    private fun areFieldsEmpty() =
        Utils.isEditTextEmpty(binding.passwordLoginEditTextField) || Utils.isEditTextEmpty(binding.emailLoginEditTextField)

    private fun changeErrorStates(emailError: Boolean = true, passwordError: Boolean = true, errorMessage: String = "") {
        with(binding) {
            vm.changeErrorStates(emailError, passwordError)
            emailLoginEditText.error = if (emailError) " " else ""
            passwordLoginEditText.error = if (passwordError) " " else ""
            loginErrorMessage.text = errorMessage
        }
    }

    private fun hideKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        binding.emailLoginEditTextField.clearFocus()
        binding.passwordLoginEditTextField.clearFocus()
        imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }
}