package com.example.reflect.presentation.screens.passwordReset.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.reflect.R
import com.example.reflect.databinding.FragmentMainPasswordResetBinding
import com.example.reflect.presentation.screens.passwordReset.viewmodel.ViewModelPasswordReset
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainPasswordResetFragment : Fragment() {
    private var _binding: FragmentMainPasswordResetBinding? = null
    private val binding get() = _binding!!

    private val vm: ViewModelPasswordReset by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMainPasswordResetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.resetPasswordBackArrow.setOnClickListener {
            if (binding.mainFragmentContainer.findNavController().currentDestination?.label.toString() == resources.getResourceEntryName(R.layout.fragment_password_reset_email)) {
                findNavController().popBackStack()
                vm.resetDataOnPopBackStack(withEmail = true)
            } else {
                binding.mainFragmentContainer.findNavController().popBackStack()
                vm.resetDataOnPopBackStack()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}