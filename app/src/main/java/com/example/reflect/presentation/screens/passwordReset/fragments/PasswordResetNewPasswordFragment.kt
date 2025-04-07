package com.example.reflect.presentation.screens.passwordReset.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.reflect.R
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = Navigation.findNavController(requireActivity(), R.id.mainFragmentContainer)

        binding.resetPasswordChangePasswordButton.setOnClickListener {
            navController.navigate(R.id.action_passwordResetNewPasswordFragment_to_mainFragment)
//            requireParentFragment().findNavController().navigate(R.id.action_passwordResetNewPasswordFragment_to_mainFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}