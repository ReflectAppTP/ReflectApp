package com.example.reflect.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.reflect.R
import com.example.reflect.common.Prefs
import com.example.reflect.databinding.DialogConsentBinding

class ConsentDialog: DialogFragment() {

    // TODO: перепроверить код чистой головой на утечки памяти

    private var _binding: DialogConsentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogConsentBinding.inflate(inflater, container, false)
        isCancelable = false
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            dialogAgreeButton.setOnClickListener {
                Prefs.setConsent(requireContext(), true)
                dismiss()
            }

            dialogDeclineButton.setOnClickListener {
                dismiss()
                requireActivity().finish()
            }
        }
    }

    override fun getTheme(): Int = R.style.DialogTheme

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}