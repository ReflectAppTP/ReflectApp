package com.example.reflect.presentation.screens.addState.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import com.example.reflect.databinding.FragmentThirdClarificationAddStateBinding
import com.example.reflect.presentation.common.Utils
import com.example.reflect.presentation.screens.addState.viewmodel.AddStateViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ThirdClarificationAddStateFragment : Fragment() {

    private val vm: AddStateViewModel by activityViewModels()

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

        bindViewModelAndTextField()
        addButtonOnClickListeners()

        view.post {
            binding.addStateThirdClarificationTextInputField.requestFocus()
            showKeyboard(binding.addStateThirdClarificationTextInputField)
        }

        binding.addStateThirdClarificationLayout.setOnClickListener { clickedView ->
            if (clickedView !is TextInputEditText) {
                hideKeyboard()
            }
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

    private fun addButtonOnClickListeners() {
        with (binding) {
            addStateThirdClarificationNextButton.setOnClickListener {
                Utils.toast(requireContext())
                // TODO Просто bruh!
                (parentFragment?.parentFragment as BottomSheetDialogFragment).dismiss()
            }
            addStateThirdClarificationTextInputField.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    addStateThirdClarificationNextButton.performClick()
                }
                true
            }
        }
    }
}