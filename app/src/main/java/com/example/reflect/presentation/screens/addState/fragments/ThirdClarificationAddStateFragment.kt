package com.example.reflect.presentation.screens.addState.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.reflect.databinding.FragmentThirdClarificationAddStateBinding
import com.example.reflect.presentation.common.ToastUtils
import com.example.reflect.presentation.screens.addState.AddStateIntent
import com.example.reflect.presentation.screens.addState.RecordState
import com.example.reflect.presentation.screens.addState.viewmodel.ViewModelAddState
import com.example.reflect.presentation.screens.records.viewmodel.ViewModelRecords
import com.example.reflect.presentation.screens.statistics.StatisticIntent
import com.example.reflect.presentation.screens.statistics.viewmodel.VIewModelStatistic
import com.example.reflect.presentation.widget.WidgetStateProvider
import com.example.reflect.presentation.widget.WidgetStreakProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import io.appmetrica.analytics.AppMetrica
import kotlinx.coroutines.launch
import kotlin.random.Random

@AndroidEntryPoint
class ThirdClarificationAddStateFragment : Fragment() {

    private val vm: ViewModelAddState by activityViewModels()
    // Говнокод
    private val recordsvm: ViewModelRecords by activityViewModels()
    private val statisticvm: VIewModelStatistic by activityViewModels()

    private var _binding: FragmentThirdClarificationAddStateBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentThirdClarificationAddStateBinding.inflate(inflater, container, false)
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

        bindViewModelAndTextField()
        addOnClickListeners()

        view.post {
            binding.addStateThirdClarificationTextInputField.requestFocus()
            showKeyboard(binding.addStateThirdClarificationTextInputField)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showKeyboard(view: View) {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun hideKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        binding.addStateThirdClarificationTextInputField.clearFocus()
        imm.hideSoftInputFromWindow(binding.addStateThirdClarificationLayout.windowToken, 0)
    }

    private fun bindViewModelAndTextField() {
        with (binding) {
            addStateThirdClarificationTextInputField.setText(vm.emotionalDescription.value)
            addStateThirdClarificationTextInputField.doAfterTextChanged { value ->
                vm.updateEmotionalDescription(value.toString())
            }
        }
    }

    private fun addOnClickListeners() {
        with (binding) {
            addStateThirdClarificationNextButton.setOnClickListener {
                lifecycleScope.launch {
                    if (vm.id.value != null) {
                        AppMetrica.reportEvent("Редактирование состояния с уточнениями")
                        vm.userIntent.send(AddStateIntent.EditState)
                    } else {
                        AppMetrica.reportEvent("Добавление состояния с уточнениями")
                        vm.userIntent.send(AddStateIntent.AddState)
                    }
                }
            }

            addStateThirdClarificationLayout.setOnClickListener { clickedView ->
                if (clickedView !is TextInputEditText) {
                    hideKeyboard()
                }
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
                // Обновляю статистику на другом фрагменте с помощью говнокоа
                lifecycleScope.launch {
                    statisticvm.userIntent.send(StatisticIntent.UpdateStatistic)
                }
                (parentFragment?.parentFragment as BottomSheetDialogFragment).dismiss()

                // Обновляю виджет
                // TODO: fix
                WidgetStreakProvider.updateWidget(requireContext(), Random.nextInt(0,10))
                // Обновляю виджет состояния
                WidgetStateProvider.updateWidget(requireContext(), state.record!!.value)
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