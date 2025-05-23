package com.mobile.gameofsecret.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mobile.gameofsecret.DestinationScreen
import com.mobile.gameofsecret.R
import com.mobile.gameofsecret.data.AdId
import com.mobile.gameofsecret.ui.components.BackHeader
import com.mobile.gameofsecret.ui.components.BannerAdCard
import com.mobile.gameofsecret.ui.theme.background
import com.mobile.gameofsecret.ui.components.NameWheel
import com.mobile.gameofsecret.ui.utils.navigateTo
import com.mobile.gameofsecret.viewmodels.GamerViewModel


@Composable
fun RandomGameScreen(gamerViewModel: GamerViewModel, navController: NavController) {

    val gamerList by gamerViewModel.gamerList.collectAsState()
    val names = gamerList.map { it.name }
    LaunchedEffect(Unit) {
        //gamerViewModel.getGamerList()
    }
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
            .padding(WindowInsets.systemBars.asPaddingValues()), topBar = {
            BackHeader(onBackClicked = {
                navigateTo(navController, route = DestinationScreen.Pre.route)
            }, headerText = stringResource(R.string.random))
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(background)
                .padding(it)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                item {
                    NameWheel(names, navController = navController, onComplete = { selectedName ->
                        navigateTo(
                            navController,
                            DestinationScreen.TruthOrDare.createRoute(selectedName)
                        )
                    })
                }
            }
            BannerAdCard(adUnitId = AdId)
            Spacer(modifier = Modifier.height(5.dp))
        }
    }
}
