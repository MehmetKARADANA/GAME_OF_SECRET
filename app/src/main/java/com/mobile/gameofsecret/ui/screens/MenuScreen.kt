package com.mobile.gameofsecret.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mobile.gameofsecret.ui.components.WheelOfFortune
import com.mobile.gameofsecret.ui.components.WheelOfFortunetest
import com.mobile.gameofsecret.ui.components.WheelSection
import com.mobile.gameofsecret.ui.theme.background

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
                        .height(300.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {

                    WheelOfFortunetest(
                        sections =  listOf(
                            WheelSection("500 TL", Color(0xFFE53935)),
                            WheelSection("200 TL", Color(0xFF43A047)),
                            WheelSection("1000 TL", Color(0xFF1E88E5)),
                            WheelSection("0 TL", Color(0xFFFFB300)),
                            WheelSection("100 TL", Color(0xFF8E24AA)),
                            WheelSection("300 TL", Color(0xFF00897B))
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
                        colors = CardColors(
                            containerColor = Color.White,
                            contentColor = Color.Gray,
                            disabledContentColor = Color.Gray,
                            disabledContainerColor = Color.Gray
                        )
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text("ADD Gamer")
                            Text("user1")
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