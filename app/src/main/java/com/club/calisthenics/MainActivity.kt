package com.club.calisthenics

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.club.calisthenics.core.ui.theme.CalisthenicsTheme
import com.club.calisthenics.feature.auth.ui.LoginScreen
import com.club.calisthenics.ui.AppViewModel
import com.club.calisthenics.ui.MainScreen
import com.club.calisthenics.ui.SplashScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CalisthenicsTheme {
                val viewModel: AppViewModel = hiltViewModel()
                val isInitializing by viewModel.isInitializing.collectAsState()
                val isLoggedIn by viewModel.isUserLoggedIn.collectAsState()
                
                when {
                    isInitializing -> SplashScreen()
                    isLoggedIn -> MainScreen()
                    else -> LoginScreen()
                }
            }
        }
    }
}
