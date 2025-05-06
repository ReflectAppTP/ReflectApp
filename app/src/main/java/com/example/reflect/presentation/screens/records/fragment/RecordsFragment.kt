package com.example.reflect.presentation.screens.records.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reflect.R
import com.example.reflect.databinding.FragmentRecordsBinding
import com.example.reflect.presentation.adapters.RecordsListAdapter
import com.example.reflect.presentation.common.ToastUtils
import com.example.reflect.presentation.screens.addState.RecordState
import com.example.reflect.presentation.screens.records.DeleteStateIntent
import com.example.reflect.presentation.screens.records.GetRecordsState
import com.example.reflect.presentation.screens.records.viewmodel.ViewModelRecords
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecordsFragment : Fragment() {

    private val vm: ViewModelRecords by activityViewModels()

    private var _binding: FragmentRecordsBinding? = null
    private val binding get() = _binding!!

    private lateinit var recordsAdapter: RecordsListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRecordsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.recordsState.collect { state ->
                    handleRecordsState(state)
                }

                vm.deleteState.collect { state ->
                    handleDeleteState(state)
                }
            }
        }

        with (binding) {
            fragmentRecordsRV.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            recordsAdapter = RecordsListAdapter(
                vm.calendar,
                onEdit = { id, model ->
                    val args = Bundle().apply {
                        putInt("id", id)
                        putParcelable("recordModel", model)
                    }
                    findNavController().navigate(R.id.addStateBottomSheetFragment, args)
                },
                onDelete = {
                    lifecycleScope.launch {
                        vm.userIntent.send(DeleteStateIntent.DeleteRecord(id))
                    }
                }
            )
            fragmentRecordsRV.adapter = recordsAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun handleRecordsState(state: GetRecordsState) {
        val context = requireContext()
        when (state) {
            is GetRecordsState.Loading -> {
                ToastUtils.showLoadingToast(context)
            }
            is GetRecordsState.Success -> {
                recordsAdapter.submitList(vm.records.value)
            }
            is GetRecordsState.Error -> {
                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
            }
            is GetRecordsState.Idle -> {
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
                ToastUtils.showDeleteStateToast(context)
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