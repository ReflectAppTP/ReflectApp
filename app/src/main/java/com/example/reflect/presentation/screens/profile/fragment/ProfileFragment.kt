package com.example.reflect.presentation.screens.profile.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
            fragmentProfileUserLogin.text = AccountPrefs.getUser(requireContext()).username

            if (AccountPrefs.isAuthorized(requireContext())) {
                fragmentProfileLogoutButton.visibility = View.VISIBLE
                fragmentProfileLoginButton.visibility = View.GONE
                fragmentProfileRegistrationButton.visibility = View.GONE
            } else if (AccountPrefs.isGuest(requireContext())) {
                fragmentProfileLogoutButton.visibility = View.GONE
                fragmentProfileLoginButton.visibility = View.VISIBLE
                fragmentProfileRegistrationButton.visibility = View.VISIBLE
                fragmentProfileImageViewChangeIcon.visibility = View.GONE
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
                // TODO: потом переделать
                Toast.makeText(requireContext(), "Тут должен быть фрагмент виджетов", Toast.LENGTH_SHORT).show()
            }

            fragmentProfilePremiumButton.setOnClickListener {
                // TODO: потом переделать
                AppMetrica.reportEvent("Нажатие на кнопку Покупка премиума")
                Toast.makeText(requireContext(), "Тут должен быть фрагмент премиума", Toast.LENGTH_SHORT).show()
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

            fragmentProfileImageViewChangeIcon.setOnClickListener {
                // TODO: потом переделать
                Toast.makeText(requireContext(), "Потом доделаю редактирование иконки профиля", Toast.LENGTH_SHORT).show()
            }

        }
    }
}