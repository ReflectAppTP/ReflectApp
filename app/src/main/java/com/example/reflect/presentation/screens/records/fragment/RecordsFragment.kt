package com.example.reflect.presentation.screens.records.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reflect.R
import com.example.reflect.databinding.FragmentRecordsBinding
import com.example.reflect.presentation.adapter.RecordsListAdapter
import com.example.reflect.presentation.common.ToastUtils
import com.example.reflect.presentation.screens.addState.RecordState
import com.example.reflect.presentation.screens.records.DeleteStateIntent
import com.example.reflect.presentation.screens.records.GetRecordsState
import com.example.reflect.presentation.screens.records.viewmodel.ViewModelRecords
import com.example.reflect.presentation.screens.statistics.StatisticIntent
import com.example.reflect.presentation.screens.statistics.viewmodel.VIewModelStatistic
import com.example.reflect.presentation.widget.WidgetStateProvider
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Calendar
import kotlin.random.Random

@AndroidEntryPoint
class RecordsFragment : Fragment() {

    private val vm: ViewModelRecords by activityViewModels()
    private val statisticvm: VIewModelStatistic by activityViewModels()

    private var _binding: FragmentRecordsBinding? = null
    private val binding get() = _binding!!

    private lateinit var recordsAdapter: RecordsListAdapter
    private lateinit var datePicker: MaterialDatePicker<Long>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRecordsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val context = requireContext()

        datePicker = createDatePicker()
        setupDatePickerListeners(datePicker)

        with (binding) {
            fragmentRecordsDateTV.setOnClickListener {
                datePicker.show(parentFragmentManager, "datePicker")
            }

            val streakPopup = StreakPopup(requireContext())
            fragmentRecordsToolbarStreakIcon.setOnClickListener {
                streakPopup.updateData(Random.nextInt(0,10))
                streakPopup.show(fragmentRecordsToolbarStreakIcon)
            }

            // Анимация для переключения даты по нажатию стрелочек
            val animationDuration = 131L
            val sir = AnimationUtils.loadAnimation(context, R.anim.slide_in_right)
            sir.duration = animationDuration
            val sor = AnimationUtils.loadAnimation(context, android.R.anim.slide_out_right)
            sor.duration = animationDuration
            val sol = AnimationUtils.loadAnimation(context, R.anim.slide_out_left)
            sol.duration = animationDuration
            val sil = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left)
            sil.duration = animationDuration

            sol.setAnimationListener(object: AnimationListener {
                override fun onAnimationStart(p0: Animation?) {

                }
                override fun onAnimationEnd(p0: Animation?) {
                    vm.mutableCalendar.roll(Calendar.DAY_OF_MONTH, 1)
                    vm.updateSelectedDate()

                    datePicker = createDatePicker()
                    setupDatePickerListeners(datePicker)

                    fragmentRecordsDateTV.startAnimation(sir)
                }
                override fun onAnimationRepeat(p0: Animation?) {
                }
            })

            sor.setAnimationListener(object: AnimationListener {
                override fun onAnimationStart(p0: Animation?) {

                }
                override fun onAnimationEnd(p0: Animation?) {
                    vm.mutableCalendar.roll(Calendar.DAY_OF_MONTH, -1)
                    vm.updateSelectedDate()

                    datePicker = createDatePicker()
                    setupDatePickerListeners(datePicker)

                    fragmentRecordsDateTV.startAnimation(sil)
                }
                override fun onAnimationRepeat(p0: Animation?) {

                }
            })
            fragmentRecordsICChevronLeft.setOnClickListener {
                fragmentRecordsDateTV.startAnimation(sor)
            }

            fragmentRecordsICChevronRight.setOnClickListener {
                fragmentRecordsDateTV.startAnimation(sol)
            }

            fragmentRecordsRV.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            recordsAdapter = RecordsListAdapter(
                vm.currentCalendar,
                onEdit = { id, model ->
                    val args = Bundle().apply {
                        putInt("id", id)
                        putParcelable("recordModel", model)
                    }
                    findNavController().navigate(R.id.addStateBottomSheetFragment, args)
                },
                onDelete = { id ->
                    lifecycleScope.launch {
                        vm.userIntent.send(DeleteStateIntent.DeleteRecord(id))
                    }
                }
            )
            fragmentRecordsRV.adapter = recordsAdapter

            // TODO: почему тут надо в разных scope
            lifecycleScope.launch {
                vm.selectedDateText.collect { date ->
                    fragmentRecordsDateTV.text = date
                }
            }

            lifecycleScope.launch {
                vm.recordsState.collect { state ->
                    handleRecordsState(state)
                }
            }

            lifecycleScope.launch {
                vm.deleteState.collect { state ->
                    handleDeleteState(state)
                }
            }
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun handleRecordsState(state: GetRecordsState) {
        recordsAdapter.updateState(state)

        when (state) {
            is GetRecordsState.Error -> ToastUtils.showErrorConnectionToast(requireContext())
            // Обновляю виджеты
            is GetRecordsState.EmptyContent -> {
                if (vm.datesAreEquals()) WidgetStateProvider.updateWidget(requireContext(), null)
            }
            is GetRecordsState.Success -> {
                if (vm.datesAreEquals()) WidgetStateProvider.updateWidget(requireContext(), state.records.first().value)
            }
            else -> {
                Unit
            }
        }
    }

    private fun handleDeleteState(state: RecordState) {
        val context = requireContext()
        when (state) {
            is RecordState.Loading -> {
                ToastUtils.showLoadingToast(context)
            }
            is RecordState.Success -> {
                vm.fetchRecords()
                ToastUtils.showDeleteStateToast(context)
                lifecycleScope.launch {
                    statisticvm.userIntent.send(StatisticIntent.UpdateStatistic)
                }
            }
            is RecordState.Error -> {
                ToastUtils.showErrorToast(context)
            }
            is RecordState.Idle -> {
                Unit
            }
        }
    }

    private fun createDatePicker(): MaterialDatePicker<Long> {
        return MaterialDatePicker.Builder.datePicker()
            .setSelection(vm.mutableCalendar.timeInMillis)
            .setTitleText(R.string.selectDate)
            .setTheme(R.style.ThemeOverlay_App_DatePicker)
            .build()
    }

    private fun setupDatePickerListeners(datePicker: MaterialDatePicker<Long>) {
        datePicker.addOnPositiveButtonClickListener {
            val selectedDate = vm.mutableCalendar.apply {
                timeInMillis = it
            }
            vm.updateSelectedDate(
                selectedDate.get(Calendar.YEAR),
                selectedDate.get(Calendar.MONTH),
                selectedDate.get(Calendar.DAY_OF_MONTH)
            )
        }
    }
}