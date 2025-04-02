package com.mobile.gameofsecret.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mobile.gameofsecret.DestinationScreen
import com.mobile.gameofsecret.ui.components.BackHeader
import com.mobile.gameofsecret.ui.components.Header
import com.mobile.gameofsecret.ui.theme.background
import com.mobile.gameofsecret.ui.theme.buttonColors1
import com.mobile.gameofsecret.ui.utils.navigateTo
import com.mobile.gameofsecret.viewmodels.GamerViewModel
import com.mobile.gameofsecret.viewmodels.QuizViewModel

@Composable
fun TruthScreen(name: String, navController: NavController,quizViewModel: QuizViewModel,fromScreen : String) {

    val question by quizViewModel.question.collectAsState()

    BackHandler {
            navigateTo(navController = navController, route = fromScreen)
    }
    Scaffold(modifier = Modifier.fillMaxSize()) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(background)
                .padding(it)
        ) {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(background),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {

                    Column(
                        modifier = Modifier
                            .padding(start = 4.dp, end = 4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ){
                        BackHeader(onBackClicked = {
                            navigateTo(navController,fromScreen)
                        },"Truth")

                        Text("$name ,soru senin :", color = Color.White)
                        Text("${question?.question}",color = Color.White)

                    }
                }
            }

        }
    }

}