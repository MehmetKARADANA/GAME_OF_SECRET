package com.mobile.gameofsecret.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.mobile.gameofsecret.DestinationScreen
import com.mobile.gameofsecret.ui.utils.navigateTo

@Composable
fun TruthScreen(name : String,navController: NavController) {
    Column {
        Text(text = "soru senin $name")
        Text(text = "truth")
        Button(onClick = {
            navigateTo(navController, DestinationScreen.RandomGame.route)
        }) { Text(text = "ok") }
    }
}