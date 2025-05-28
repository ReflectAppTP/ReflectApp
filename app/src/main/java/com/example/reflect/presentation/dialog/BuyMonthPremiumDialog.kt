package com.example.reflect.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.reflect.R
import com.example.reflect.databinding.DialogBuyMonthPremiumBinding

class BuyMonthPremiumDialog : DialogFragment() {

    private var _binding: DialogBuyMonthPremiumBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogBuyMonthPremiumBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with (binding) {
            dialogAgreeButton.setOnClickListener {

                dismiss()
            }

            dialogDeclineButton.setOnClickListener {

                dismiss()
            }
        }
    }

    override fun getTheme(): Int = R.style.DialogTheme

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}