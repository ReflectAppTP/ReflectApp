package com.example.reflect.presentation.screens.addState.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.reflect.R
import com.example.reflect.databinding.FragmentMainAddStateBinding
import com.example.reflect.presentation.common.ToastUtils
import com.example.reflect.presentation.screens.addState.AddStateIntent
import com.example.reflect.presentation.screens.addState.RecordState
import com.example.reflect.presentation.screens.addState.viewmodel.ViewModelAddState
import com.example.reflect.presentation.screens.records.viewmodel.ViewModelRecords
import com.example.reflect.presentation.screens.statistics.StatisticIntent
import com.example.reflect.presentation.screens.statistics.viewmodel.VIewModelStatistic
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import io.appmetrica.analytics.AppMetrica
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainAddStateFragment : Fragment() {

    private val vm: ViewModelAddState by activityViewModels()
    // Говнокод
    private val recordsvm: ViewModelRecords by activityViewModels()
    private val statisticvm: VIewModelStatistic by activityViewModels()

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

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.recordState.collect { state ->
                    handleRecordState(state)
                }
            }
        }

        changeSliderState(vm.emotionalState.value)
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
                lifecycleScope.launch {
                    if (vm.id.value != null) {
                        AppMetrica.reportEvent("Редактирование состояния")
                        vm.userIntent.send(AddStateIntent.EditState)
                    } else {
                        AppMetrica.reportEvent("Добавление состояния без уточнения")
                        vm.userIntent.send(AddStateIntent.AddState)

                    }
                }
            }

            addStateClarifyButton.setOnClickListener {
                AppMetrica.reportEvent("Нажатие на кнопку Первое уточнение")
                findNavController().navigate(R.id.action_mainAddStateFragment_to_firstClarificationAddStateFragment)
            }
        }
    }

    private fun handleRecordState(state: RecordState) {
        val context = requireContext()
        when (state) {
            is RecordState.Loading -> {
                ToastUtils.showLoadingToast(context)
            }
            is RecordState.Success -> {
                if (vm.id.value != null) {
                    ToastUtils.showEditStateToast(context)
                } else {
                    ToastUtils.showAddStateToast(context)
                }
                recordsvm.fetchRecords()
                lifecycleScope.launch {
                    statisticvm.userIntent.send(StatisticIntent.UpdateStatistic)
                }
                (parentFragment?.parentFragment as BottomSheetDialogFragment).dismiss()
            }
            is RecordState.Error -> {
                ToastUtils.showErrorToast(context)
            }
            is RecordState.Idle -> {
                Unit
            }
        }
    }
}