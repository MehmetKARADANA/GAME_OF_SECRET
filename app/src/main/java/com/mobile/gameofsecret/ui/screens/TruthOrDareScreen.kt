package com.mobile.gameofsecret.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mobile.gameofsecret.DestinationScreen
import com.mobile.gameofsecret.ui.theme.background
import com.mobile.gameofsecret.ui.theme.cardcolor
import com.mobile.gameofsecret.ui.utils.navigateTo


@Composable
fun TruthOrDareScreen(name: String,navController: NavController) {
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
                    .background(background)
            ) {
                item {
                    Column(modifier = Modifier.padding(start = 4.dp, end = 4.dp)) {

                            Card (modifier = Modifier.fillMaxWidth()
                                .wrapContentHeight()
                                .padding(4.dp)
                            , colors = cardcolor,
                                onClick = {
                                    navigateTo(navController = navController,DestinationScreen.Truth.createRoute(name))
                                }
                            ){
                                Row (modifier = Modifier.padding(8.dp)){
                                    Text(text = "Truth")
                                }

                        }
                        Card (modifier = Modifier.fillMaxWidth()
                            .wrapContentHeight()
                            .padding(4.dp)
                            , colors = cardcolor,
                            onClick = {
                                navigateTo(navController = navController,DestinationScreen.Dare.createRoute(name))
                            }
                        ){
                            Row (modifier = Modifier.padding(8.dp)){
                                Text(text = "Dare")
                            }

                        }



                    }
                }
            }
        }
    }
}