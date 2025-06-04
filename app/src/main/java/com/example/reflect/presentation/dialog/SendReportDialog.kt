package com.example.reflect.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.reflect.R
import com.example.reflect.databinding.DialogSendReportBinding

class SendReportDialog(
    private val userId: Int,
    private val userName: String,
    private val onSendReport: (Int, String) -> Unit
) : DialogFragment() {

    private var _binding: DialogSendReportBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogSendReportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with (binding) {
            dialogSendReportDescription.text = requireContext().resources.getString(R.string.dialogSendReportDescription, userName)


            dialogSendReportRadioProfileReport.isChecked = true
            dialogSendReportRadioProfileReport.setOnCheckedChangeListener  { _, isChecked ->
                dialogSendReportRadioProfileReport.isChecked = isChecked
            }
            dialogSendReportRadioStateReport.setOnCheckedChangeListener  { _, isChecked ->
                dialogSendReportRadioStateReport.isChecked = isChecked
            }


            dialogSendReportButton.setOnClickListener {
                onSendReport(
                    userId,
                    if (dialogSendReportRadioStateReport.isChecked) "Неприемлимое описание карточки состояния"
                    else "Неприемлимое имя пользователя"
                )
                dismiss()
            }

            dialogBackButton.setOnClickListener {
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