package com.mobile.gameofsecret.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mobile.gameofsecret.DestinationScreen
import com.mobile.gameofsecret.ui.theme.background
import com.mobile.gameofsecret.ui.theme.buttonColors1
import com.mobile.gameofsecret.ui.utils.navigateTo

@Composable
fun TruthScreen(name: String, navController: NavController) {
    BackHandler {
        navigateTo(navController = navController,DestinationScreen.RandomGame.route)
    }
    Scaffold(modifier = Modifier.fillMaxSize()) {
        Surface(modifier = Modifier
            .fillMaxSize()
            .background(background)
            .padding(it)) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        background
                    )
                    .padding(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "soru senin $name")
                Text(text = "truth")
                Button(onClick = {
                    navigateTo(navController, DestinationScreen.RandomGame.route)
                }, colors = buttonColors1, modifier = Modifier.fillMaxWidth()) { Text(text = "ok") }
            }
        }
    }

}