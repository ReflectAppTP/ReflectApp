package com.example.reflect.presentation.screens.addState.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.reflect.R
import com.example.reflect.databinding.FragmentSecondClarificationAddStateBinding
import com.example.reflect.presentation.adapters.AddStateTagListAdapter
import com.example.reflect.presentation.screens.addState.viewmodel.AddStateViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SecondClarificationAddStateFragment : Fragment() {

    private val vm: AddStateViewModel by activityViewModels()

    private var _binding: FragmentSecondClarificationAddStateBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSecondClarificationAddStateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with (binding) {
            addStateSecondClarificationRV.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            addStateSecondClarificationRV.adapter = AddStateTagListAdapter(
                vm.secondTags.value!!,
                vm.selectedSecondTags.value!!,
                vm::addTagIdToSecondList
            )
        }

        addButtonOnClickListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun addButtonOnClickListeners() {
        with (binding) {
            addStateSecondClarificationNextButton.setOnClickListener {
                findNavController().navigate(R.id.action_secondClarificationAddStateFragment_to_thirdClarificationAddStateFragment)
            }
        }
    }

}