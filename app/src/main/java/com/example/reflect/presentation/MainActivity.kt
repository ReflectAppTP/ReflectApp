package com.example.reflect.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
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

        // TODO: обернуть в отдельную функцию
        val bottomNavBar = binding.bottomNavigationBar
        val navHostFragment = supportFragmentManager.findFragmentById(binding.fragmentContainerView.id) as NavHostFragment
        val navController = navHostFragment.navController
        bottomNavBar.setupWithNavController(navController)
        bottomNavBar.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.add_record -> {
                    Toast.makeText(this, "Мяу мяу...", Toast.LENGTH_SHORT).show()
                    false
                }
                else -> {
                    navController.navigate(item.itemId)
                    true
                }
            }
        }
    }
}