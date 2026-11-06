package com.rojas.consumodatos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.rojas.consumodatos.ui.navigation.AppNavigation
import com.rojas.consumodatos.ui.theme.ConsumoDatosTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ConsumoDatosTheme {
                AppNavigation()
            }
        }
    }
}
