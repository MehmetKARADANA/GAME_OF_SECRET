package com.mobile.gameofsecret.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mobile.gameofsecret.DestinationScreen
import com.mobile.gameofsecret.R
import com.mobile.gameofsecret.data.AdId
import com.mobile.gameofsecret.ui.components.BackHeader
import com.mobile.gameofsecret.ui.components.BannerAdCard
import com.mobile.gameofsecret.ui.components.LargeButton
import com.mobile.gameofsecret.ui.theme.background
import com.mobile.gameofsecret.ui.theme.buttonColors1
import com.mobile.gameofsecret.ui.utils.navigateTo
import com.mobile.gameofsecret.viewmodels.QuizViewModel

@Composable
fun DareScreen(
    name: String,
    navController: NavController,
    quizViewModel: QuizViewModel,
    fromScreen: String
) {

    val infiniteTransition = rememberInfiniteTransition(label = "Truth")
    val animatedSize by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "Truth"
    )
    val isNullQuestion = stringResource(R.string.loading_question)
    val dareQuestion by quizViewModel.dareQuestion.collectAsState()
    BackHandler {
        navigateTo(navController = navController, fromScreen)
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
            .padding(WindowInsets.systemBars.asPaddingValues()),
        topBar = {
            BackHeader(onBackClicked = {
                navigateTo(navController, fromScreen)
            }, stringResource(R.string.dare))
        }) {
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
                    .background(background),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {

                    Column(
                        modifier = Modifier
                            .padding(start = 8.dp, end = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .padding(top = 50.dp)
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
                        Text(
                            text = stringResource(R.string.your_question),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            modifier = Modifier.padding(16.dp),
                            text = "${dareQuestion?.question}" ?: isNullQuestion,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        LargeButton(text = stringResource(R.string.okey)) {
                            navigateTo(navController = navController, route = fromScreen)
                        }
                    }
                }
            }
            BannerAdCard(adUnitId = AdId)
            Spacer(Modifier.height(5.dp))
        }
    }
}