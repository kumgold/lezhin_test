package com.example.lezhintest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import com.example.lezhintest.ui.main.MainScreen
import com.example.lezhintest.ui.theme.LezhinTestTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val config = resources.configuration
        config.setLocale(Locale.getDefault())
        createConfigurationContext(config)

        setContent {
            val widthSizeClass = calculateWindowSizeClass(this).widthSizeClass

            LezhinTestTheme {
                MainScreen(widthSizeClass)
            }
        }
    }
}