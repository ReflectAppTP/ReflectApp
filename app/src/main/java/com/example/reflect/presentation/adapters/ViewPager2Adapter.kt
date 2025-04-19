package com.example.reflect.presentation.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.reflect.presentation.screens.friends.fragment.FriendsFragment
import com.example.reflect.presentation.screens.profile.fragment.ProfileFragment
import com.example.reflect.presentation.screens.records.fragment.RecordsFragment
import com.example.reflect.presentation.screens.statistics.fragment.StatisticsFragment

class MainFragmentViewPagerAdapter(
    fragment: Fragment
) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment =
        when(position) {
            0 -> RecordsFragment()
            1 -> StatisticsFragment()
            2 -> FriendsFragment()
            3 -> ProfileFragment()
            else -> throw IllegalArgumentException("Invalid position $position")
        }

}