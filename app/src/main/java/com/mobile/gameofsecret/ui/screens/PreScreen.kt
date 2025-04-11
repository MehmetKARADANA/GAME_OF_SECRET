package com.mobile.gameofsecret.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mobile.gameofsecret.DestinationScreen
import com.mobile.gameofsecret.R
import com.mobile.gameofsecret.ui.components.FAB
import com.mobile.gameofsecret.ui.components.Header
import com.mobile.gameofsecret.ui.theme.background
import com.mobile.gameofsecret.ui.theme.cardcolor
import com.mobile.gameofsecret.ui.theme.textColor
import com.mobile.gameofsecret.ui.utils.navigateTo
import com.mobile.gameofsecret.viewmodels.GamerViewModel
import com.mobile.gameofsecret.viewmodels.QuizViewModel

enum class GameTypes(val type: String, val route: String) {
    RANDOM(type = "Random", route = DestinationScreen.RandomGame.route),
    SERIAL(type = "Serial", route = DestinationScreen.SerialGame.route),
    SPIN(type = "Spin Bottle", route = DestinationScreen.SpinBottle.route)
}

@Composable
fun PreScreen(gamerViewModel: GamerViewModel, navController: NavController,quizViewModel: QuizViewModel) {

    LaunchedEffect(Unit) {
       // gamerViewModel.getGamerList()
    }

    val selectedGameType = remember { mutableStateOf(DestinationScreen.RandomGame.route) }
    val selectedType = remember { mutableStateOf(GameTypes.RANDOM.type) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(background), floatingActionButton = {
            FAB(onClick = {
                navigateTo(navController, selectedGameType.value)
            }, text = "Play Game")
        }, floatingActionButtonPosition = FabPosition.Center
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
                    .background(background),
            ) {
                item {
                    Header(navController)
                }
                item {
                    Column(modifier = Modifier.padding(start = 4.dp, end = 4.dp)) {
                        for (type in GameTypes.entries) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                                    .padding(4.dp).
                                    border(
                                      width  = if (selectedType.value==type.type) 2.dp else 0.dp,
                                        color = if (selectedType.value==type.type) Color.Cyan else Color.Transparent,
                                        shape = RoundedCornerShape(8.dp)
                            ),
                                colors = cardcolor,
                                elevation = CardDefaults.elevatedCardElevation(12.dp),
                            onClick = {
                                //selectroute oluşutracak navigate etmeyecek
                                //tskmenudeki matııkla seçili olanın borderını renklendir
                                selectedGameType.value = type.route
                                selectedType.value = type.type
                            }
                            ) {
                                Row(modifier = Modifier.padding(8.dp)) {
                                    Text(text = type.type)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}