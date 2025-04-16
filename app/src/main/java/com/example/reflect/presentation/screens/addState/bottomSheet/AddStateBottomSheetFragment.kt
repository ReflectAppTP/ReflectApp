package com.example.reflect.presentation.screens.addState.bottomSheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.reflect.R
import com.example.reflect.databinding.FragmentAddStateBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddStateBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentAddStateBottomSheetBinding? = null
    private val binding get() = _binding!!

    override fun onStart() {
        super.onStart()
        val dialog = dialog as? BottomSheetDialog ?: return

        val bottomSheet =
            dialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)

        if (bottomSheet != null) {
            val behavior = BottomSheetBehavior.from(bottomSheet)
            val layoutParams = bottomSheet.layoutParams
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            bottomSheet.layoutParams = layoutParams

            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.skipCollapsed = true
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddStateBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with (binding) {
            addStateBackArrow.setOnClickListener {
                if (addStateFragmentContainerView.findNavController().currentDestination?.label.toString() == resources.getResourceEntryName(R.layout.fragment_main_add_state)) {
//                    findNavController().popBackStack()
                    dismiss()
                } else {
                    addStateFragmentContainerView.findNavController().popBackStack()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}