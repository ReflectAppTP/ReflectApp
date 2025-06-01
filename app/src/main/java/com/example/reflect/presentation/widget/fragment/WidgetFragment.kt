package com.example.reflect.presentation.widget.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.reflect.R
import com.example.reflect.databinding.FragmentWidgetBinding
import com.example.reflect.presentation.common.ToastUtils
import com.example.reflect.presentation.dialog.AddWidgetToHomeScreenDialog
import com.example.reflect.presentation.widget.WidgetStateProvider
import com.example.reflect.presentation.widget.WidgetStreakProvider

class WidgetFragment : Fragment() {

    private var _binding: FragmentWidgetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentWidgetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with (binding) {
            val dialog = AddWidgetToHomeScreenDialog()

            fragmentWidgetToggleGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
                if (isChecked) {
                    when (checkedId) {
                        R.id.fragmentWidgetStreakButton -> {
                            fragmentWidgetStreakLayout.visibility = View.VISIBLE
                            fragmentWidgetStateLayout.visibility = View.GONE
                            dialog.onClick = {
                                ToastUtils.showAddWidget(requireContext())
                                WidgetStreakProvider.pinWidget(requireContext())
                            }
                        }
                        R.id.fragmentWidgetStateButton -> {
                            fragmentWidgetStreakLayout.visibility = View.GONE
                            fragmentWidgetStateLayout.visibility = View.VISIBLE
                            dialog.onClick = {
                                ToastUtils.showAddWidget(requireContext())
                                WidgetStateProvider.pinWidget(requireContext())
                            }
                        }
                    }
                } else {
                    if (-1 == group.checkedButtonId) {
                        group.check(checkedId)
                    }
                }
            }
            fragmentWidgetStreakButton.performClick()

            fragmentWidgetToolbarBackArrow.setOnClickListener {
                findNavController().popBackStack()
            }

            fragmentWidgetStreakLayout.setOnClickListener{
                dialog.show(parentFragmentManager, "Show add to home dialog")
            }

            fragmentWidgetStateLayout.setOnClickListener {
                dialog.show(parentFragmentManager, "Show add to home dialog")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}