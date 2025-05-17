package com.mobile.gameofsecret.ui.screens

import android.util.Log
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
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
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
import com.mobile.gameofsecret.data.AdId
import com.mobile.gameofsecret.ui.components.BackHeader
import com.mobile.gameofsecret.ui.components.BannerAdCard
import com.mobile.gameofsecret.ui.theme.background
import com.mobile.gameofsecret.ui.theme.cardcolor
import com.mobile.gameofsecret.ui.theme.cardcolor2
import com.mobile.gameofsecret.ui.theme.cardcolor3
import com.mobile.gameofsecret.ui.utils.navigateTo
import com.mobile.gameofsecret.viewmodels.GamerViewModel
import com.mobile.gameofsecret.viewmodels.QuizViewModel
import kotlinx.coroutines.launch


@Composable
fun SerialGameScreen(
    navController: NavController,
    gamerViewModel: GamerViewModel,
    quizViewModel: QuizViewModel
) {

    val fromScreen = DestinationScreen.SerialGame.route
    val currentGamer = gamerViewModel.currentGamer.value

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
        Column (
            modifier = Modifier
                .fillMaxSize()
                .background(background)
                .padding(it)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .background(background),// verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BackHeader(onBackClicked = {
                    navigateTo(
                        navController,
                        DestinationScreen.Pre.route
                    )
                }, headerText = stringResource(R.string.serial))

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 4.dp, end = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(R.string.your_turn) + "!",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp) // Sabit bir yükseklik vererek yukarı-aşağı kaymasını engelle
                    ) {
                        Text(
                            text = currentGamer?.name ?: "",
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

                                quizViewModel.getRandomTruthQuestion()
                                navigateTo(
                                    navController = navController,
                                    DestinationScreen.Truth.createRoute(
                                        currentGamer?.name ?: "",
                                        fromScreen = fromScreen
                                    )
                                )
                                gamerViewModel.nextPlayer()

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

                                //to do dare getrandom
                                quizViewModel.getRandomDareQuestion()

                                navigateTo(
                                    navController = navController,
                                    DestinationScreen.Dare.createRoute(
                                        currentGamer?.name ?: "",
                                        fromScreen = fromScreen
                                    )
                                )
                                gamerViewModel.nextPlayer()

                            Log.d("sgs", "current player ${currentGamer?.name}")
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
                            //to do getrandom random
                            //gözden kaçırma if iiçnde hata oluyor mu randomquestion
                            //  quizViewModel.getRandomDareQuestion()
                            val randomNumber = (0..10).random()
                            if (randomNumber % 2 == 0) {

                                    quizViewModel.getRandomTruthQuestion()
                                    navigateTo(
                                        navController = navController,
                                        DestinationScreen.Truth.createRoute(
                                            fromScreen = "serial",
                                            name = currentGamer?.name ?: fromScreen
                                        )
                                    )

                            } else {

                                    quizViewModel.getRandomDareQuestion()
                                    navigateTo(
                                        navController = navController,
                                        DestinationScreen.Dare.createRoute(
                                            fromScreen = "serial",
                                            name = currentGamer?.name ?: fromScreen
                                        )
                                    )

                            }
                            gamerViewModel.nextPlayer()
                        },
                        elevation = CardDefaults.elevatedCardElevation(12.dp),
                    ) {
                        Row(modifier = Modifier.padding(8.dp)) {
                            Text(text = stringResource(R.string.random))
                        }

                    }


                }
            }
            BannerAdCard(adUnitId = AdId)
            Spacer(Modifier.height(5.dp))
        }
    }
}