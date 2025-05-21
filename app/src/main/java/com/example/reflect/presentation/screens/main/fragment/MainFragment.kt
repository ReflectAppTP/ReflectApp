package com.example.reflect.presentation.screens.main.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.reflect.R
import com.example.reflect.databinding.FragmentMainBinding
import com.example.reflect.presentation.adapters.MainFragmentViewPagerAdapter
import dagger.hilt.android.AndroidEntryPoint
import io.appmetrica.analytics.AppMetrica

@AndroidEntryPoint
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)

        val viewPager = binding.mainActivityViewPager
        viewPager.adapter = MainFragmentViewPagerAdapter(this)
        viewPager.isUserInputEnabled = false

        val bottomNavBar = binding.bottomNavBar
        bottomNavBar.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.recordsFragment -> {
                    viewPager.currentItem = 0
                    true
                }
                R.id.statisticsFragment -> {
                    AppMetrica.reportEvent("Переход на экран статистики")
                    viewPager.currentItem = 1
                    true
                }
                R.id.add_record -> {
                    AppMetrica.reportEvent("Нажатие на кнопку добавления состояния")
                    findNavController().navigate(R.id.addStateBottomSheetFragment)
                    false
                }
                R.id.friendsFragment -> {
                    AppMetrica.reportEvent("Переход на экран друзей")
                    viewPager.currentItem = 2
                    true
                }
                R.id.profileFragment -> {
                    viewPager.currentItem = 3
                    true
                }
                else -> false
            }
        }

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val destinationId = when (position) {
                    0 -> R.id.recordsFragment
                    1 -> R.id.statisticsFragment
                    2 -> R.id.friendsFragment
                    3 -> R.id.profileFragment
                    else -> R.id.recordsFragment
                }

                bottomNavBar.selectedItemId = destinationId
            }
        })

        binding.fabAI.setOnClickListener {
            AppMetrica.reportEvent("Нажатие на кнопку ИИ")
            val options = FragmentNavigatorExtras(binding.fabAI to "fab_transition")
            findNavController().navigate(
                R.id.aiFragment,
                null,
                null,
                options
            )
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}