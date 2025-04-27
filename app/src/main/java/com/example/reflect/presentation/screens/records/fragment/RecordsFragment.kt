package com.example.reflect.presentation.screens.records.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reflect.databinding.FragmentRecordsBinding
import com.example.reflect.presentation.adapters.RecordsListAdapter
import com.example.reflect.presentation.screens.records.viewmodel.ViewModelRecords
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecordsFragment : Fragment() {

    private val vm: ViewModelRecords by viewModels()

    private var _binding: FragmentRecordsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRecordsBinding.inflate(inflater, container, false)
        vm.fetchRecords()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with (binding) {
            fragmentRecordsRV.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            fragmentRecordsRV.adapter = RecordsListAdapter(
                vm.records.value!!,
                vm.calendar,
                {
                    vm::updateRecord
                    Toast.makeText(requireContext(), "Обновить запись", Toast.LENGTH_SHORT).show()
                },
                {
                    vm::deleteRecord
                    Toast.makeText(requireContext(), "Удалить", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}