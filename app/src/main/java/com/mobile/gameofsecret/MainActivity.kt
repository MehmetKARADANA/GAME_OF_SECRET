package com.mobile.gameofsecret

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

import com.mobile.gameofsecret.ui.components.WheelOfFortuneScreen
import com.mobile.gameofsecret.ui.theme.GameofsecretTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GameofsecretTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                   Surface(modifier = Modifier.padding(innerPadding)) {
WheelOfFortuneScreen()
                   }
                }
            }
        }
    }
}

