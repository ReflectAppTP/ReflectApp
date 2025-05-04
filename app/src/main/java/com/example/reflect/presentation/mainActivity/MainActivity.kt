package com.example.reflect.presentation.mainActivity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.reflect.R
import com.example.reflect.common.prefs.AccountPrefs
import com.example.reflect.common.prefs.ConsentPrefs
import com.example.reflect.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Timer
import kotlin.concurrent.schedule

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var splashScreen: SplashScreen
    private lateinit var binding: ActivityMainBinding
    private val vm: ViewModelMainActivity by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        splashScreen = installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(binding.mainFragmentContainer.id) as NavHostFragment
        val navController = navHostFragment.navController

        lifecycleScope.launch {
            vm.state.collect { state ->
                handleRefreshState(navController, state)
            }
        }

        val accessToken = AccountPrefs.getAuthToken(this)
        val refreshToken = AccountPrefs.getRefreshToken(this)

        if (accessToken != null && refreshToken != null) {
            splashScreen.setKeepOnScreenCondition { true }
            vm.validateUser(accessToken, refreshToken)
        }

        if (!ConsentPrefs.hasConsent(this)) {
            navController.navigate(R.id.consentDialog)
        }

    }

    private fun handleRefreshState(navController: NavController ,state: GetProfileState) {
        when (state) {
            is GetProfileState.Loading -> {
                splashScreen.setKeepOnScreenCondition { true }
            }
            is GetProfileState.Success -> {
                AccountPrefs.saveUserToken(this, state.loginModel.access, state.loginModel.refresh)
                navController.navigate(R.id.action_loginFragment_to_mainFragment)
                // TODO: ГОВНОКОД!
                Timer("SettingUp", false).schedule(1000) {
                    splashScreen.setKeepOnScreenCondition { false }
                }

            }
            is GetProfileState.Error -> {
                // TODO: Какой нибудь тост сделать
                Toast.makeText(this, "Какая то ошибка на тосте", Toast.LENGTH_SHORT).show()
                splashScreen.setKeepOnScreenCondition { false }
            }
            is GetProfileState.RefreshError -> {
                AccountPrefs.clearTokens(this)
                splashScreen.setKeepOnScreenCondition { false }
            }
            is GetProfileState.Idle -> Unit
        }
    }

}
