package com.example.reflect.presentation.screens.ai.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reflect.databinding.FragmentAiBinding
import com.example.reflect.presentation.screens.ai.adapter.AIHelperTextAdapter
import com.example.reflect.presentation.screens.ai.viewmodel.ViewModelAI
import com.google.android.material.transition.MaterialContainerTransform
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AiFragment : Fragment() {

    private var _binding: FragmentAiBinding? = null
    private val binding get() = _binding!!

    private val vm: ViewModelAI by activityViewModels()

    private lateinit var helperTextAdapter: AIHelperTextAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            duration = 300
            isElevationShadowEnabled = true
        }

        sharedElementReturnTransition = MaterialContainerTransform().apply {
            duration = 500
            isElevationShadowEnabled = true
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with (binding) {

            aiEditTextField.setText(vm.inputTextValue.value)
            aiEditTextField.doAfterTextChanged { value ->
                vm.updateText(value.toString())
            }

            aiToolbarBackArrow.setOnClickListener {
                findNavController().popBackStack()
            }

            aiHelperTextsRV.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            helperTextAdapter = AIHelperTextAdapter(
                onClick = { 
                    vm.updateTextWithHelper(it)
                    aiEditTextField.append("$it ")
                } 
            )
            helperTextAdapter.submitList(vm.helperTextList.value)
            aiHelperTextsRV.adapter = helperTextAdapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}