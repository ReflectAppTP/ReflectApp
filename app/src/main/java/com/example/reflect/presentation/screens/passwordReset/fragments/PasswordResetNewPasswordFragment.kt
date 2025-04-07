package com.example.reflect.presentation.screens.passwordReset.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.reflect.databinding.FragmentPasswordResetNewPasswordBinding
import com.example.reflect.presentation.screens.passwordReset.viewmodel.ViewModelPasswordReset

class PasswordResetNewPasswordFragment : Fragment() {

    private var _binding: FragmentPasswordResetNewPasswordBinding? = null
    private val binding get() = _binding!!

    private val vm: ViewModelPasswordReset by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentPasswordResetNewPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}