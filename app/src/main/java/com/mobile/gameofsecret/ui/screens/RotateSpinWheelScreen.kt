package com.mobile.gameofsecret.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.mobile.gameofsecret.R
import com.mobile.gameofsecret.ui.components.BackHeader
import com.mobile.gameofsecret.ui.components.NameWheel
import com.mobile.gameofsecret.ui.utils.navigateTo
import com.mobile.gameofsecret.viewmodels.TaskViewModel

@Composable
fun RotateSpinWheelScreen(taskViewModel: TaskViewModel, navController: NavController) {
    val taskList by taskViewModel.taskList.collectAsState()
    val tasks= taskList.map { it.task }

    LaunchedEffect(Unit) {

    }

    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        BackHeader(
            onBackClicked = {
                navController.popBackStack()
            }, headerText = stringResource(
                R.string.spin_the_wheel
            )
        )
    }) {
        Surface(modifier = Modifier.padding(it)) {
            Column(modifier = Modifier.fillMaxSize()) {
                NameWheel(tasks, navController = navController, onComplete ={} )
            }
        }
    }
}