package com.bagmanovam.rikiandmorti

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.bagmanovam.rikiandmorti.presentation.theme.RikiAndMortiTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppContent()
        }
    }
}

@Composable
fun AppContent() {
    RikiAndMortiTheme {
        val navHostController = rememberNavController()

        RikMortiNavHost(
            navHostController = navHostController
        )

    }
}