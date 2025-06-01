package com.example.reflect.presentation.widget.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.reflect.R
import com.example.reflect.databinding.FragmentWidgetBinding

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
            fragmentWidgetToggleGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
                if (isChecked) {
                    when (checkedId) {
                        R.id.fragmentWidgetStreakButton -> {
                            fragmentWidgetStreakLayout.visibility = View.VISIBLE
                            fragmentWidgetStateLayout.visibility = View.GONE
                        }
                        R.id.fragmentWidgetStateButton -> {
                            fragmentWidgetStreakLayout.visibility = View.GONE
                            fragmentWidgetStateLayout.visibility = View.VISIBLE
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
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}