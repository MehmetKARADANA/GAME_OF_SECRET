package com.mobile.gameofsecret.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mobile.gameofsecret.DestinationScreen
import com.mobile.gameofsecret.R
import com.mobile.gameofsecret.ui.components.GameAnimationFromRaw
import com.mobile.gameofsecret.ui.components.Header
import com.mobile.gameofsecret.ui.theme.background
import com.mobile.gameofsecret.ui.theme.cardcolor
import com.mobile.gameofsecret.ui.theme.textColor
import com.mobile.gameofsecret.ui.utils.getGameTypeDescription
import com.mobile.gameofsecret.ui.utils.getGameTypeImage
import com.mobile.gameofsecret.ui.utils.getGameTypeName
import com.mobile.gameofsecret.ui.utils.navigateTo
import com.mobile.gameofsecret.viewmodels.GamerViewModel
import com.mobile.gameofsecret.viewmodels.QuizViewModel
import kotlinx.coroutines.launch


enum class GameTypes(val type: String, val route: String) {
    RANDOM(type = "Random", route = DestinationScreen.RandomGame.route),
    SERIAL(type = "Serial", route = DestinationScreen.SerialGame.route),
    SPIN(type = "Spin Wheel", route = DestinationScreen.SpinWheel.route),
    GROUP(type = "Group Game", route = DestinationScreen.CreateGroupGame.route)
}

@Composable
fun PreScreen(
    gamerViewModel: GamerViewModel,
    navController: NavController,
    quizViewModel: QuizViewModel
) {
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
            .padding(WindowInsets.systemBars.asPaddingValues()),
        topBar = {
            Header(navController, stringResource(R.string.app_name))
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
                    .background(background),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Üst kısım - Animasyon
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        GameAnimationFromRaw(
                            rawResId = R.raw.thinking_people,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

                // Başlık
                item {
                    Text(
                        text = stringResource(R.string.select_game_mode),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.W600,
                        color = textColor,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                    )
                }

                // Oyun modu kartları
                item {
                    Column(modifier = Modifier.padding(horizontal = 12.dp)) {
                        for (type in GameTypes.entries) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                                    .padding(vertical = 5.dp),
                                colors = cardcolor,
                                shape = RoundedCornerShape(12.dp),
                                elevation = CardDefaults.elevatedCardElevation(4.dp),
                                onClick = {
                                    if (type == GameTypes.GROUP) {
                                        navigateTo(navController, DestinationScreen.CreateGroupGame.route)
                                    } else {
                                        // MenuScreen'in hangi oyun modunda olduğunu bilmesi için
                                        gamerViewModel.selectedGameType.value = type.route
                                        navigateTo(navController, DestinationScreen.Menu.createRoute(type.route))
                                        scope.launch {
                                            quizViewModel.getRandomDareQuestion()
                                            quizViewModel.getRandomTruthQuestion()
                                        }
                                    }
                                }
                            ) {
                                val typeName = getGameTypeName(type)
                                val typeDesc = getGameTypeDescription(type)
                                val typeImage = getGameTypeImage(type)

                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Image(
                                        painter = painterResource(typeImage),
                                        contentDescription = typeName,
                                        modifier = Modifier.size(50.dp)
                                    )
                                    Column(
                                        modifier = Modifier.padding(start = 12.dp)
                                    ) {
                                        Text(
                                            text = typeName,
                                            fontWeight = FontWeight.W600,
                                            fontSize = 15.sp,
                                            color = textColor
                                        )
                                        Text(
                                            text = typeDesc,
                                            fontWeight = FontWeight.W300,
                                            fontSize = 12.sp,
                                            color = textColor.copy(alpha = 0.7f)
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Join with Code button
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .padding(vertical = 2.dp),
                            colors = cardcolor,
                            shape = RoundedCornerShape(10.dp),
                            elevation = CardDefaults.elevatedCardElevation(6.dp),
                            onClick = {
                                navigateTo(navController, DestinationScreen.JoinGroupGame.createRoute(null))
                            }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "🔗 ${stringResource(R.string.join_with_code)}",
                                    fontSize = 14.sp,
                                    color = textColor,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }
           // BannerAdCard(adUnitId = AdId)
            Spacer(Modifier.height(16.dp))
        }
    }
}