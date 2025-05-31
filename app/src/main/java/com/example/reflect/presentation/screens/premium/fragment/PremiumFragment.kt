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
import com.example.reflect.common.prefs.AccountPrefs
import com.example.reflect.databinding.FragmentPremiumBinding
import com.example.reflect.presentation.dialog.BuyMonthPremiumDialog
import com.example.reflect.presentation.dialog.BuyYearPremiumDialog
import com.example.reflect.presentation.dialog.CancelPremiumDialog
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
            if (AccountPrefs.isPremium(requireContext())) {
                fragmentPremiumCatImage.setImageResource(R.drawable.image_buy_premium_happy_cat)
                fragmentPremiumStatusTitle.text = resources.getString(R.string.fragmentPremiumStatusTitle, "А СУДЬИ КТО")
                fragmentPremiumStatusTitle.visibility = View.VISIBLE
                fragmentPremiumCancelSub.visibility = View.VISIBLE

                fragmentPremiumMonthPriceCard.setOnClickListener {
                    fragmentPremiumCatImage.performClick()
                }

                fragmentPremiumYearPriceCard.setOnClickListener {
                    fragmentPremiumCatImage.performClick()
                }
            } else {
                fragmentPremiumCatImage.setImageResource(R.drawable.image_buy_premium)
                fragmentPremiumStatusTitle.visibility = View.GONE
                fragmentPremiumCancelSub.visibility = View.GONE

                fragmentPremiumMonthPriceCard.setOnClickListener {
                    val dialog = BuyMonthPremiumDialog {
                        // TODO: Добавить usecase от Ромы
                        AccountPrefs.saveUserModel(requireContext(), AccountPrefs.getUser(requireContext()).copy(isPremium = true))
                        findNavController().popBackStack()
                    }
                    dialog.show(parentFragmentManager, "Show month premium dialog")
                }

                fragmentPremiumYearPriceCard.setOnClickListener {
                    val dialog = BuyYearPremiumDialog {
                        // TODO: Добавить usecase от Ромы
                        AccountPrefs.saveUserModel(requireContext(), AccountPrefs.getUser(requireContext()).copy(isPremium = true))
                        findNavController().popBackStack()
                    }
                    dialog.show(parentFragmentManager, "Show year premium dialog")
                }
            }

            fragmentPremiumToolbarBackArrow.setOnClickListener {
                findNavController().popBackStack()
            }

            fragmentPremiumCancelSub.setOnClickListener {
                val dialog = CancelPremiumDialog {
                    AccountPrefs.saveUserModel(requireContext(), AccountPrefs.getUser(requireContext()).copy(isPremium = false))
                    findNavController().popBackStack()
                }
                dialog.show(parentFragmentManager, "Show cancel premium dialog")
            }

            fragmentPremiumCatImage.setOnClickListener {
                val scaleDownY = ObjectAnimator.ofFloat(it, "scaleY", 0.85f).apply {
                    duration = 70
                }
                val scaleUpY = ObjectAnimator.ofFloat(it, "scaleY", 1f).apply {
                    duration = 70
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