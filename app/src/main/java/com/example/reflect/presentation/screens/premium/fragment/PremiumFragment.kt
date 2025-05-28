package com.example.reflect.presentation.screens.premium.fragment

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.reflect.R
import com.example.reflect.databinding.FragmentPremiumBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PremiumFragment : Fragment() {

    private var _binding: FragmentPremiumBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPremiumBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with (binding) {
            fragmentPremiumToolbarBackArrow.setOnClickListener {
                findNavController().popBackStack()
            }

            fragmentPremiumMonthPriceCard.setOnClickListener {
                findNavController().navigate(R.id.buyMonthPremiumDialog)
            }

            fragmentPremiumYearPriceCard.setOnClickListener {
                findNavController().navigate(R.id.buyYearPremiumDialog)
            }

            fragmentPremiumCatImage.setOnClickListener {
                val scaleDownY = ObjectAnimator.ofFloat(it, "scaleY", 0.8f).apply {
                    duration = 100
                }
                val scaleUpY = ObjectAnimator.ofFloat(it, "scaleY", 1f).apply {
                    duration = 100
                }
                AnimatorSet().apply {
                    play(scaleDownY).before(scaleUpY)
                    start()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}