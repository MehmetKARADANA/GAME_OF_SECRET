package com.mobile.gameofsecret.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.mobile.gameofsecret.ui.theme.background
import com.mobile.gameofsecret.ui.components.NameWheel
import com.mobile.gameofsecret.viewmodels.GamerViewModel


@Composable
fun RandomGameScreen(gamerViewModel: GamerViewModel,navController: NavController) {

    val gamerList by gamerViewModel.gamerList.collectAsState()
LaunchedEffect(Unit) {
    //gamerViewModel.getGamerList()
}
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(background)
                .padding(it)
        ) {
            NameWheel(gamerList, navController = navController)
        }
    }
}
