package com.example.reflect.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.reflect.R
import com.example.reflect.databinding.ActivityMainBinding
import com.example.reflect.presentation.adapters.ViewPager2Adapter

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // TODO: обернуть в отдельную функцию
        val viewPager = binding.mainActivityViewPager
        viewPager.adapter = ViewPager2Adapter(this)
        viewPager.isUserInputEnabled = false

        val bottomNavBar = binding.bottomNavBar
        bottomNavBar.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.recordsFragment -> {
                    viewPager.currentItem = 0
                    true
                }
                R.id.statisticsFragment -> {
                    viewPager.currentItem = 1
                    true
                }
                R.id.add_record -> {
                    Toast.makeText(this, "Мяу мяу...", Toast.LENGTH_SHORT).show()
                    false
                }
                R.id.friendsFragment -> {
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
    }
}