package com.example.reflect.presentation.screens.profile.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.reflect.R
import com.example.reflect.common.prefs.AccountPrefs
import com.example.reflect.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint
import io.appmetrica.analytics.AppMetrica

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with (binding) {
            if (AccountPrefs.isPremium(requireContext())) {
                fragmentProfilePremiumIcon.visibility = View.VISIBLE
            } else {
                fragmentProfilePremiumIcon.visibility = View.GONE
            }

            fragmentProfileUserLogin.text = AccountPrefs.getUser(requireContext()).username

            if (AccountPrefs.isAuthorized(requireContext())) {
                fragmentProfileLogoutButton.visibility = View.VISIBLE
                fragmentProfileLoginButton.visibility = View.GONE
                fragmentProfileRegistrationButton.visibility = View.GONE
                fragmentProfilePremiumButton.visibility = View.VISIBLE
                fragmentProfileWidgetButton.visibility = View.VISIBLE
                fragmentProfileToolbarSettingsIcon.visibility = View.VISIBLE
            } else if (AccountPrefs.isGuest(requireContext())) {
                fragmentProfileLogoutButton.visibility = View.GONE
                fragmentProfileLoginButton.visibility = View.VISIBLE
                fragmentProfileRegistrationButton.visibility = View.VISIBLE
                fragmentProfilePremiumButton.visibility = View.GONE
                fragmentProfileWidgetButton.visibility = View.GONE
                fragmentProfileToolbarSettingsIcon.visibility = View.GONE
            }
        }

        setOnClickLogic()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setOnClickLogic() {
        with(binding) {
            fragmentProfileWidgetButton.setOnClickListener {
                findNavController().navigate(R.id.action_mainFragment_to_widgetFragment)
            }

            fragmentProfilePremiumButton.setOnClickListener {
                AppMetrica.reportEvent("Нажатие на кнопку Покупка премиума")
                findNavController().navigate(R.id.action_profileFragment_to_premiumFragment)
            }

            fragmentProfileRulesButton.setOnClickListener {
                findNavController().navigate(R.id.action_mainFragment_to_rulesFragment)
            }

            fragmentProfileLoginButton.setOnClickListener {
                findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
            }

            fragmentProfileRegistrationButton.setOnClickListener {
                findNavController().navigate(R.id.action_profileFragment_to_registrationFragment)
            }

            fragmentProfileLogoutButton.setOnClickListener {
                findNavController().navigate(R.id.logoutDialog)
            }

            fragmentProfileToolbarSettingsIcon.setOnClickListener {
                findNavController().navigate(R.id.action_mainFragment_to_profileSettingsFragment)
            }
        }
    }
}