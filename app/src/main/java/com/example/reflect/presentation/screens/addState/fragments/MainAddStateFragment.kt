package com.example.reflect.presentation.screens.addState.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.reflect.R
import com.example.reflect.databinding.FragmentMainAddStateBinding
import com.example.reflect.presentation.common.Utils
import com.example.reflect.presentation.screens.addState.viewmodel.AddStateViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainAddStateFragment : Fragment() {

    private val vm: AddStateViewModel by activityViewModels()

    private var _binding: FragmentMainAddStateBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMainAddStateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        changeSliderState(vm.emotionalState.value!!)
        addSliderOnChangeListener()
        addButtonOnClickListeners()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun addSliderOnChangeListener() {
        with (binding) {
            addStateSlider.addOnChangeListener { _, value, _ ->
                changeSliderState(value)
            }
        }
    }

    private fun changeSliderState(value: Float) {
        with (binding) {
            addStateSlider.value = value
            vm.updateEmotionalState(value)
            when (value.toInt()) {
                in 0..1 -> addStateImage.setImageResource(R.drawable.ic_add_state_emotion_1)
                in 2..3 -> addStateImage.setImageResource(R.drawable.ic_add_state_emotion_2)
                in 4..6 -> addStateImage.setImageResource(R.drawable.ic_add_state_emotion_3)
                in 7..8 -> addStateImage.setImageResource(R.drawable.ic_add_state_emotion_4)
                in 9..10 -> addStateImage.setImageResource(R.drawable.ic_add_state_emotion_5)
                else -> throw IllegalStateException("Как так вообще получилось, что значение от 0 до 10 больше 10?!")
            }
        }
    }

    private fun addButtonOnClickListeners() {
        with (binding) {
            addStateSaveButton.setOnClickListener {
                Utils.toast(requireContext())
                // TODO: просто bruh
                (parentFragment?.parentFragment as BottomSheetDialogFragment).dismiss()
            }

            addStateClarifyButton.setOnClickListener {
                findNavController().navigate(R.id.action_mainAddStateFragment_to_firstClarificationAddStateFragment)
            }
        }
    }
}