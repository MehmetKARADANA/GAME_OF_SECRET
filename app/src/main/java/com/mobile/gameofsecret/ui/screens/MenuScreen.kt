package com.mobile.gameofsecret.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mobile.gameofsecret.DestinationScreen
import com.mobile.gameofsecret.R
import com.mobile.gameofsecret.ui.components.EmptyWheelOfFortune
import com.mobile.gameofsecret.ui.components.FAB
import com.mobile.gameofsecret.ui.components.Header
import com.mobile.gameofsecret.ui.components.WheelSection
import com.mobile.gameofsecret.ui.theme.background
import com.mobile.gameofsecret.ui.theme.cardcolor
import com.mobile.gameofsecret.ui.theme.textColor
import com.mobile.gameofsecret.ui.theme.textFieldColor
import com.mobile.gameofsecret.ui.utils.navigateTo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(navController: NavController) {

    BackHandler {
        navController.popBackStack()
    }

    Scaffold(modifier = Modifier
        .fillMaxSize()
        .background(background), topBar = {
        // Header(navController = navController)
    }, floatingActionButton = {
        FAB(onClick = {}, text = "Starting")
    }, floatingActionButtonPosition = FabPosition.Center
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(background)//bunu kaldırınca statusbar kendi rengine dönüyor
                .padding(it)
        ) {
            val scrollState = rememberScrollState()
            val userFields = remember { mutableStateListOf("Oyuncu 1", "Oyuncu 2") }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    // .verticalScroll(scrollState)
                    .background(background)
                    .padding(12.dp),
            ) {

                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .background(color = background),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.information),
                            contentDescription = "information",
                            tint = Color.White,
                            modifier = Modifier
                                .padding(start = 12.dp)
                                .size(24.dp)
                                .clickable {
                                    navigateTo(
                                        navController = navController,
                                        route = DestinationScreen.Settings.route
                                    )
                                }
                        )
                        Text(
                            text = "GOS",
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.W500,
                            fontSize = 22.sp,
                            color = textColor
                        )
                        Icon(
                            painter = painterResource(R.drawable.setting),
                            contentDescription = "Settings",
                            tint = Color.White,
                            modifier = Modifier
                                .padding(end = 12.dp)
                                .size(24.dp)
                                .clickable {
                                    navigateTo(
                                        navController = navController,
                                        route = DestinationScreen.Settings.route
                                    )
                                }
                        )
                    }
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
                                WheelSection("500 TL", Color(0xFFE53935)),
                                WheelSection("200 TL", Color(0xFF43A047)),
                                WheelSection("1000 TL", Color(0xFF1E88E5)),
                                WheelSection("0 TL", Color(0xFFFFB300)),
                                WheelSection("100 TL", Color(0xFF8E24AA)),
                                WheelSection("300 TL", Color(0xFF00897B)),
                                WheelSection("500 TL", Color(0xFFE53935)),
                                WheelSection("0 TL", Color(0xFFFFB300)),
                                WheelSection("1000 TL", Color(0xFF1E88E5)),
                            ),
                            rotationAngle = 0f, // Burada sabit bir açı verilmiş
                            modifier = Modifier.fillMaxSize()
                        )

                    }
                }
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

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
                                    "Add Gamer",
                                    fontSize = 22.sp,
                                    color = textColor,
                                    modifier = Modifier.padding(8.dp)
                                )

                                LaunchedEffect(userFields.size) {
                                    if (userFields.size < 2) {
                                        userFields.add("Oyuncu ${userFields.size + 1}")
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
                                            label = { Text("Oyuncu ${index + 1}") },
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
                                        "✚  Oyuncu ekle",
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .wrapContentHeight()
                                            .padding(24.dp, bottom = 24.dp, top = 8.dp)
                                            .clickable {
                                                val newUserIndex = userFields.size + 1
                                                userFields.add("Oyuncu $newUserIndex")
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

@Composable
fun CommonDivider() {
    HorizontalDivider(
        color = Color.LightGray,
        thickness = 1.dp,
        modifier = Modifier
            .alpha(0.5f)
            .padding(top = 8.dp, bottom = 8.dp)
    )
}