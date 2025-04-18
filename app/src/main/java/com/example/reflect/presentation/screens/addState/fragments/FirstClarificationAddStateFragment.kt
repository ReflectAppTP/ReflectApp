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
import com.example.reflect.databinding.FragmentFirstClarificationAddStateBinding
import com.example.reflect.presentation.adapters.AddStateTagListAdapter
import com.example.reflect.presentation.screens.addState.viewmodel.AddStateViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FirstClarificationAddStateFragment : Fragment() {

    private val vm: AddStateViewModel by activityViewModels()

    private var _binding: FragmentFirstClarificationAddStateBinding? = null
    private val binding get() = _binding!!

    private fun addTagIdToFirstList(id: Int) {
        vm.addTagIdToFirstList(id)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFirstClarificationAddStateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with (binding) {
//            calculateSpanCount()
            addStateFirstClarificationRV.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            addStateFirstClarificationRV.adapter = AddStateTagListAdapter(vm.firstTags.value!!.toList(), ::addTagIdToFirstList )
        }

        addButtonOnClickListeners()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun addButtonOnClickListeners() {
        with (binding) {
            addStateFirstClarificationNextButton.setOnClickListener {
                findNavController().navigate(R.id.action_firstClarificationAddStateFragment_to_secondClarificationAddStateFragment)
            }
        }
    }
}