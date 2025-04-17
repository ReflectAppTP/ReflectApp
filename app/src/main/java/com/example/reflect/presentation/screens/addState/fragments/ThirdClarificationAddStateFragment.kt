package com.example.reflect.presentation.screens.addState.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.example.reflect.databinding.FragmentThirdClarificationAddStateBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.internal.ViewUtils.showKeyboard

class ThirdClarificationAddStateFragment : Fragment() {

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

        view.post {
            binding.addStateThirdClarificationTextInputField.requestFocus()
            showKeyboard(binding.addStateThirdClarificationTextInputField)
        }

        addButtonOnClickListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showKeyboard(view: View) {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun addButtonOnClickListeners() {
        with (binding) {
            addStateThirdClarificationNextButton.setOnClickListener {
                // TODO: переделать тост и внести строку в ресурсы
                Toast.makeText(context, "Запись сохранена", Toast.LENGTH_SHORT).show()
                // TODO Просто bruh!
                (parentFragment?.parentFragment as BottomSheetDialogFragment).dismiss()
            }
        }
    }
}