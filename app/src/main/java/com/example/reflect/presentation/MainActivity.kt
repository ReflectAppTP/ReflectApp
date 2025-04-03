package com.example.reflect.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.reflect.R
import com.example.reflect.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // TODO: обернуть в отдельную функцию
        val bottomNavBar = binding.bottomNavBar
        val navHostFragment = supportFragmentManager.findFragmentById(binding.fragmentContainerView.id) as NavHostFragment
        val navController = navHostFragment.navController
        bottomNavBar.setupWithNavController(navController)
        bottomNavBar.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.recordsFragment -> {
                    navController.navigate(it.itemId)
                    true
                }
                R.id.statisticsFragment -> {
                    navController.navigate(it.itemId)
                    true
                }
                R.id.add_record -> {
                    Toast.makeText(this, "Мяу мяу...", Toast.LENGTH_SHORT).show()
                    false
                }
                R.id.friendsFragment -> {
                    navController.navigate(it.itemId)
                    true
                }
                R.id.profileFragment -> {
                    navController.navigate(it.itemId)
                    true
                }
                else -> false
            }
        }
    }
}