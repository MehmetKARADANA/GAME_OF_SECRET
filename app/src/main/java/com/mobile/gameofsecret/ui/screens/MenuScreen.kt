package com.mobile.gameofsecret.ui.screens


import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FabPosition
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mobile.gameofsecret.DestinationScreen
import com.mobile.gameofsecret.R
import com.mobile.gameofsecret.data.model.Gamer
import com.mobile.gameofsecret.ui.components.EmptyWheelOfFortune
import com.mobile.gameofsecret.ui.components.FAB
import com.mobile.gameofsecret.ui.components.Header
import com.mobile.gameofsecret.ui.components.WheelSection
import com.mobile.gameofsecret.ui.theme.background
import com.mobile.gameofsecret.ui.theme.cardcolor
import com.mobile.gameofsecret.ui.theme.textColor
import com.mobile.gameofsecret.ui.theme.textFieldColor
import com.mobile.gameofsecret.ui.utils.navigateTo
import com.mobile.gameofsecret.viewmodels.GamerViewModel

@Composable
fun MenuScreen(navController: NavController, gamerViewModel: GamerViewModel) {

    BackHandler {
        navController.popBackStack()
    }
    val gamer1 = stringResource(R.string.gamer1)
    val gamer2 = stringResource(R.string.gamer2)
    val userFields = remember {
        mutableStateListOf(gamer1, gamer2)
    }
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(background), floatingActionButton = {
            FAB(onClick = {
                gamerViewModel.resetGamers(userFields) {
                    navigateTo(navController, DestinationScreen.Pre.route)
                }
            }, text = stringResource(R.string.start))
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
                    .background(background)
                // .padding(12.dp),
            ) {

                item {
                    Header(navController)
                }
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {

                        EmptyWheelOfFortune(
                            sections = listOf(
                                WheelSection( Color(0xFFE53935)),
                                WheelSection( Color(0xFF43A047)),
                                WheelSection( Color(0xFF1E88E5)),
                                WheelSection( Color(0xFFFFB300)),
                                WheelSection( Color(0xFF8E24AA)),
                                WheelSection( Color(0xFF00897B)),
                                WheelSection( Color(0xFFE53935)),
                                WheelSection( Color(0xFFFFB300)),
                                WheelSection( Color(0xFF1E88E5)),
                            ),
                            rotationAngle = 0f, // Burada sabit bir açı verilmiş
                            modifier = Modifier.fillMaxSize()
                        )

                    }
                }
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val gamer =stringResource(R.string.gamer)
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            colors = cardcolor,
                            elevation = CardDefaults.elevatedCardElevation(12.dp)
                        ) {

                            Column(
                                modifier = Modifier
                                    .padding(8.dp)
                            ) {
                                Text(
                                    stringResource(R.string.gamers),
                                    fontSize = 22.sp,
                                    color = textColor,
                                    modifier = Modifier.padding(8.dp)
                                )

                                LaunchedEffect(userFields.size) {
                                    if (userFields.size < 2) {
                                        userFields.add("$gamer ${userFields.size + 1}")
                                        //message min 2 user
                                    }
                                }
                                Column(
                                    modifier = Modifier
                                        .padding(top = 8.dp)
                                        .fillMaxWidth()
                                        .wrapContentHeight(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    userFields.forEachIndexed { index, s ->
                                        TextField(
                                            value = s,
                                            label = { Text("${stringResource(R.string.gamer)} ${index + 1}") },
                                            onValueChange = { text ->
                                                userFields[index] = text
                                            },
                                            modifier = Modifier.padding(8.dp),
                                            colors = textFieldColor(),
                                            trailingIcon = {
                                                Icon(
                                                    painter = painterResource(R.drawable.close),
                                                    contentDescription = "Delete",
                                                    tint = Color.White,
                                                    modifier = Modifier
                                                        .size(16.dp)
                                                        .clickable {
                                                            userFields.removeAt(index)
                                                        }
                                                )
                                            }
                                        )

                                    }
                                    Text(
                                        "✚  ${stringResource(R.string.add_gamer)}",
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .wrapContentHeight()
                                            .padding(24.dp, bottom = 24.dp, top = 8.dp)
                                            .clickable {
                                                val newUserIndex = userFields.size + 1
                                                userFields.add("$gamer $newUserIndex")
                                            })
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(200.dp))
                    }
                }
            }
        }
    }
}