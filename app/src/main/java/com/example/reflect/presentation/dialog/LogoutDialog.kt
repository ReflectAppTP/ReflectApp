package com.example.reflect.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.example.reflect.R
import com.example.reflect.common.prefs.AccountPrefs
import com.example.reflect.databinding.DialogLogoutBinding

class LogoutDialog : DialogFragment() {

    private var _binding: DialogLogoutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = DialogLogoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            dialogBackButton.setOnClickListener {
                dismiss()
            }

            dialogLogoutButton.setOnClickListener {
                findNavController().navigate(R.id.action_profileFragment_to_loginFragment_with_popUp)
                AccountPrefs.clearAuthState(requireContext())
            }
        }
    }

    override fun getTheme(): Int = R.style.DialogTheme

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}