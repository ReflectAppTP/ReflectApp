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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.reflect.R
import com.example.reflect.common.Utils
import com.example.reflect.common.prefs.AccountPrefs
import com.example.reflect.databinding.FragmentLoginBinding
import com.example.reflect.presentation.common.ToastUtils
import com.example.reflect.presentation.screens.login.LoginIntent
import com.example.reflect.presentation.screens.login.LoginState
import com.example.reflect.presentation.screens.login.viewmodel.ViewModelLogin
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import io.appmetrica.analytics.AppMetrica
import kotlinx.coroutines.launch
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

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.state.collect { state ->
                    handleLoginState(state)
                }
            }
        }

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
//        // TODO: impl
//        val vkAuthCallback = object : VKIDAuthCallback {
//            override fun onAuth(accessToken: AccessToken) {
//                val token = accessToken.token
//                Log.d("VK", token)
//                Log.d("VK", accessToken.idToken.toString())
//                Log.d("VK", accessToken.userID.toString())
//                Log.d("VK", accessToken.expireTime.toString())
//                binding.loginWithVKButton.isEnabled = true
//                lifecycleScope.launch {
//                    VKID.instance.getUserData(callback = object : VKIDGetUserCallback {
//                        override fun onFail(fail: VKIDGetUserFail) {
//                            Log.d("VK", fail.description)
//                        }
//
//                        override fun onSuccess(user: VKIDUser) {
//                            Log.d("VK", user.email.toString())
//                            Log.d("VK", user.firstName.toString())
//                            Log.d("VK", user.lastName.toString())
//                        }
//
//                    })
//                }
//            }
//
//            override fun onFail(fail: VKIDAuthFail) {
//                binding.loginWithVKButton.isEnabled = true
//                ToastUtils.showErrorToast(requireContext())
//            }
//
//        }

        with(binding) {
            loginButton.setOnClickListener {
                hideKeyboard()
                if (areFieldsEmpty()) {
                    changeErrorStates(errorMessage = getText(R.string.emptyFieldsErrorMessage).toString())
                } else {
                    changeErrorStates(emailError = false, passwordError = false)
                    AppMetrica.reportEvent("Нажатие на кнопку войти")
                    lifecycleScope.launch {
                        vm.userIntent.send(LoginIntent.LoginUser)
                    }
                }
            }

            loginLikeGuestButton.setOnClickListener {
                lifecycleScope.launch {
                    vm.userIntent.send(LoginIntent.LoginLikeGuest)
                }
                AppMetrica.reportEvent("Нажатие на кнопку Войти как гость")
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

            loginRegistrationButton.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
            }

            loginForgotPasswordButton.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_resetPasswordFragment)
            }

//            loginWithVKButton.setOnClickListener {
//                loginWithVKButton.isEnabled = false
//                VKID.instance.authorize(this@LoginFragment, vkAuthCallback, params = VKIDAuthParams {
//                    scopes = setOf("phone", "email")
//                })
//            }
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

    private fun handleLoginState(state: LoginState) {
        val context = requireContext()
        when (state) {
            is LoginState.Loading -> {
                binding.loginButton.isEnabled = false
                ToastUtils.showLoadingToast(context)
            }
            is LoginState.SuccessLogin -> {
                AccountPrefs.saveUserToken(context, state.loginModel.access, state.loginModel.refresh)
            }
            is LoginState.SuccessGetProfile -> {
                AccountPrefs.saveAuthState(context, true)
                AccountPrefs.saveUserModel(context, state.userModel)
                ToastUtils.showWelcomeToast(context)
                findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
            }
            is LoginState.SuccessGuestLogin -> {
                AccountPrefs.saveAuthState(context, false)
                AccountPrefs.saveUserToken(context, state.guestModel.access!!, state.guestModel.refresh!!)
                AccountPrefs.saveUserModel(context, state.guestModel)
                ToastUtils.showWelcomeToast(context)
                findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
            }
            is LoginState.Error -> {
                binding.loginButton.isEnabled = true
                changeErrorStates(errorMessage = state.message)
                AccountPrefs.clearTokens(context)
            }
            is LoginState.Idle -> {
                Unit
            }
        }
    }
}