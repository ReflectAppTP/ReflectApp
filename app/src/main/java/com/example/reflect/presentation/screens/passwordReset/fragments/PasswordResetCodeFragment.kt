package com.example.reflect.presentation.screens.passwordReset.fragments

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.reflect.R
import com.example.reflect.databinding.FragmentPasswordResetCodeBinding
import com.example.reflect.presentation.screens.passwordReset.viewmodel.ViewModelPasswordReset

class PasswordResetCodeFragment : Fragment() {

    private var _binding: FragmentPasswordResetCodeBinding? = null
    private val binding get() = _binding!!

    private val vm: ViewModelPasswordReset by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPasswordResetCodeBinding.inflate(inflater, container, false)

        binding.resetPasswordSendCodeButton.setOnClickListener {
            findNavController().navigate(R.id.action_passwordResetCodeFragment_to_passwordResetNewPasswordFragment)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}