package com.example.reflect.presentation.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.reflect.presentation.friends.fragment.FriendsFragment
import com.example.reflect.presentation.profile.fragment.ProfileFragment
import com.example.reflect.presentation.records.fragment.RecordsFragment
import com.example.reflect.presentation.statistics.fragment.StatisticsFragment

class ViewPager2Adapter(
    fragmentActivity: FragmentActivity
) : FragmentStateAdapter(fragmentActivity) {
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