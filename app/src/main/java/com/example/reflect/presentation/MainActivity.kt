package com.example.reflect.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.reflect.R
import com.example.reflect.databinding.ActivityMainBinding
import com.example.reflect.presentation.friends.fragment.FriendsFragment
import com.example.reflect.presentation.profile.fragment.ProfileFragment
import com.example.reflect.presentation.records.fragment.RecordsFragment
import com.example.reflect.presentation.statistics.fragment.StatisticsFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bnv = binding.bottomNavigationBar

        // TODO: Переделать навигацию 
        val rf = RecordsFragment()
        val sf = StatisticsFragment()
        val ff = FriendsFragment()
        val pf = ProfileFragment()

        bnv.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.records -> {
                    setCurrentFragment(rf)
                    true
                }
                R.id.statistic -> {
                    setCurrentFragment(sf)
                    true
                }
                R.id.friends -> {
                    setCurrentFragment(ff)
                    true
                }
                R.id.profile -> {
                    setCurrentFragment(pf)
                    true
                }
                R.id.add_record -> {
                    Toast.makeText(this, "Мяу мяу...", Toast.LENGTH_SHORT).show()
                    false
                    }
                else -> false
            }
        }
    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.main_activity_frame_layout, fragment)
            commit()
        }

}