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
import com.mobile.gameofsecret.R
import com.mobile.gameofsecret.data.AdId
import com.mobile.gameofsecret.ui.components.BackHeader
import com.mobile.gameofsecret.ui.components.BannerAdCard
import com.mobile.gameofsecret.ui.components.NameWheel
import com.mobile.gameofsecret.ui.theme.background
import com.mobile.gameofsecret.ui.utils.navigateTo
import com.mobile.gameofsecret.viewmodels.TaskViewModel

@Composable
fun RotateSpinWheelScreen(taskViewModel: TaskViewModel, navController: NavController) {
    val taskList by taskViewModel.taskList.collectAsState()
    val tasks = taskList.map { it.task }

    LaunchedEffect(Unit) {

    }

    Scaffold(
        modifier = Modifier
            .background(background)
            .fillMaxSize()
            .padding(WindowInsets.systemBars.asPaddingValues()),
        topBar = {
            BackHeader(
                onBackClicked = {
                    navController.popBackStack()
                }, headerText = stringResource(
                    R.string.spin_the_wheel
                )
            )
        }) {
        Column(modifier = Modifier
            .background(background)
            .fillMaxSize()
            .padding(it)) {
            Column(modifier = Modifier
                .fillMaxSize()
                .weight(1f)) {
                NameWheel(tasks, navController = navController, onComplete = {})
            }
            BannerAdCard(adUnitId = AdId)
            Spacer(modifier = Modifier.height(5.dp))
        }
    }
}