package com.mobile.gameofsecret.ui.screens

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mobile.gameofsecret.ui.components.EmptyWheelOfFortune
import com.mobile.gameofsecret.ui.components.WheelSection
import com.mobile.gameofsecret.ui.theme.background
import com.mobile.gameofsecret.ui.theme.cardcolor
import com.mobile.gameofsecret.ui.theme.textColor
import com.mobile.gameofsecret.ui.theme.textFieldColor

@Composable
fun MenuScreen() {
    Scaffold(modifier = Modifier
        .fillMaxSize()
        .background(background), topBar = {
        // Header()
    }) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(background)//bunu kaldırınca statusbar kendi rengine dönüyor
                .padding(it)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(background)
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Text("transparent toolbar")
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp),
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
                        ),
                        rotationAngle = 0f, // Burada sabit bir açı verilmiş
                        modifier = Modifier.fillMaxSize()
                    )

                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
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
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text(
                                "Add Gamer",
                                fontSize = 22.sp,
                                color = textColor,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Box {
                                TextField(
                                    value = "User 1",
                                    onValueChange = {},
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .wrapContentHeight(),
                                    label = { Text(text = "user 1") },
                                    colors = textFieldColor()
                                )
                            }
                            Text("user 2")
                        }
                    }
                    Spacer(modifier = Modifier.padding(8.dp))
                    ElevatedButton(onClick = {}, modifier = Modifier.fillMaxWidth()) {
                        Text(text = "Start")
                    }
                }
            }
        }
    }
}