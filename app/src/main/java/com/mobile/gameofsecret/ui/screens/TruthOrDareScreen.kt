package com.mobile.gameofsecret.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mobile.gameofsecret.DestinationScreen
import com.mobile.gameofsecret.R
import com.mobile.gameofsecret.ui.components.FAB
import com.mobile.gameofsecret.ui.theme.background
import com.mobile.gameofsecret.ui.theme.cardcolor
import com.mobile.gameofsecret.ui.theme.cardcolor2
import com.mobile.gameofsecret.ui.theme.cardcolor3
import com.mobile.gameofsecret.ui.utils.navigateTo
import com.mobile.gameofsecret.viewmodels.QuizViewModel


@Composable
fun TruthOrDareScreen(name: String, navController: NavController,quizViewModel: QuizViewModel) {

    val fromScreen = DestinationScreen.RandomGame.route

    val infiniteTransition = rememberInfiniteTransition(label = "TruthOrDare")
    val animatedSize by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "TruthOrDare"
    )
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
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(background), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
            ) {

                item {

                    Column(
                        modifier = Modifier
                            .padding(start = 4.dp, end = 4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = stringResource(R.string.your_turn)+"!",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        ) {
                            Text(
                                text = name,
                                fontSize = (28.sp * animatedSize),
                                fontWeight = FontWeight.W600,
                                color = Color.Yellow
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .padding(4.dp),
                            colors = cardcolor2,
                            onClick = {
                                //truth
                                quizViewModel.getRandomQuestion()
                                navigateTo(
                                    navController = navController,
                                    DestinationScreen.Truth.createRoute(name, fromScreen = fromScreen)
                                )
                            },
                            elevation = CardDefaults.elevatedCardElevation(12.dp),
                        ) {
                            Row(modifier = Modifier.padding(8.dp)) {
                                Text(text = stringResource(R.string.truth))
                            }

                        }
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .padding(4.dp),
                            colors = cardcolor3,
                            onClick = {
                                quizViewModel.getRandomQuestion()
                                navigateTo(
                                    navController = navController,
                                    DestinationScreen.Dare.createRoute(name, fromScreen = fromScreen)
                                )
                            },
                            elevation = CardDefaults.elevatedCardElevation(12.dp),
                        ) {
                            Row(modifier = Modifier.padding(8.dp)) {
                                Text(text = stringResource(R.string.dare))
                            }
                        }
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .padding(4.dp),
                            colors = cardcolor,
                            onClick = {
                                quizViewModel.getRandomQuestion()
                                val randomNumber =(0..10).random()
                                if(randomNumber %2  == 0){
                                    navigateTo(
                                        navController = navController,
                                        DestinationScreen.Truth.createRoute(name, fromScreen = fromScreen)
                                    )
                                }else{
                                    navigateTo(
                                        navController = navController,
                                        DestinationScreen.Dare.createRoute(name, fromScreen = fromScreen)
                                    )
                                }

                            },
                            elevation = CardDefaults.elevatedCardElevation(12.dp),
                        ) {
                            Row(modifier = Modifier.padding(8.dp)) {
                                Text(text = stringResource(R.string.random))
                            }

                        }


                    }
                }
            }
        }
    }
}