package com.example.reflect.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.fragment.NavHostFragment
import com.example.reflect.R
import com.example.reflect.common.AccountPrefs
import com.example.reflect.common.ConsentPrefs
import com.example.reflect.databinding.ActivityMainBinding
import com.example.reflect.presentation.dialog.ConsentDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var splashScreen: SplashScreen
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        splashScreen = installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val navHostFragment = supportFragmentManager
            .findFragmentById(binding.mainFragmentContainer.id) as NavHostFragment
        val navController = navHostFragment.navController

        if (AccountPrefs.isLoggedIn(this)){
            navController.navigate(R.id.action_loginFragment_to_mainFragment)
        }
        if (!ConsentPrefs.hasConsent(this)) {
            navController.navigate(R.id.consentDialog)
        }

    }

}
