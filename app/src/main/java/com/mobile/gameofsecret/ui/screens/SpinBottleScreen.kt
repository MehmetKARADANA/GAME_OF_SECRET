package com.mobile.gameofsecret.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mobile.gameofsecret.ui.theme.background

@Composable
fun SpinBottleScreen() {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
    ) {
        Surface(modifier = Modifier
            .fillMaxSize()
            .background(background)
            .padding(it)) {
            Text("SpinGameScreen")
        }
    }
}