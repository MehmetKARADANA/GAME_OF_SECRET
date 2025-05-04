package com.mobile.gameofsecret.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mobile.gameofsecret.R
import com.mobile.gameofsecret.ui.components.BackHeader
import com.mobile.gameofsecret.ui.components.EmptyWheelOfFortune
import com.mobile.gameofsecret.ui.components.NameWheel
import com.mobile.gameofsecret.ui.components.WheelSection
import com.mobile.gameofsecret.ui.theme.background
import com.mobile.gameofsecret.ui.theme.cardcolor
import com.mobile.gameofsecret.ui.theme.sectorColors
import com.mobile.gameofsecret.ui.theme.textColor

@Composable
fun SpinWheelScreen(navController: NavController) {
    val field1 = stringResource(R.string.task1)
    val field2 = stringResource(R.string.task2)
    val field3 = stringResource(R.string.task3)
    var fields = remember {
        mutableStateListOf(field1, field2, field3)
    }
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        BackHeader(onBackClicked = {
            navController.popBackStack()
        }, headerText = stringResource(R.string.spin_the_wheel))
    }) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .background(background)
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
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
                                WheelSection(Color(0xFFE53935)),
                                WheelSection(Color(0xFF43A047)),
                                WheelSection(Color(0xFF1E88E5)),
                                WheelSection(Color(0xFFFFB300)),
                                WheelSection(Color(0xFF8E24AA)),
                                WheelSection(Color(0xFF00897B)),
                                WheelSection(Color(0xFFE53935)),
                                WheelSection(Color(0xFFFFB300)),
                                WheelSection(Color(0xFF1E88E5)),
                            ),
                            rotationAngle = 0f, // Burada sabit bir açı verilmiş
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
                item {
                    Column(modifier = Modifier.fillMaxSize().padding(12.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally) {
                        val task= stringResource(R.string.task)
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            colors = cardcolor,
                            elevation = CardDefaults.elevatedCardElevation(12.dp)
                        ){
                            Column(
                                modifier = Modifier
                                    .padding(8.dp)
                            ) {
                                Text(
                                    stringResource(R.string.tasks),
                                    fontSize = 22.sp,
                                    color = textColor,
                                    modifier = Modifier.padding(8.dp)
                                )
                              //  LaunchedEffect() { }

                            }
                        }
                    }
                }
            }

        }
    }
}