package com.mobile.gameofsecret.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mobile.gameofsecret.viewmodels.GamerViewModel

@Composable
fun PreScreen(gamerViewModel: GamerViewModel) {
    gamerViewModel.getItemList()
    Scaffold (modifier = Modifier.fillMaxSize()){
        Surface(modifier = Modifier.padding(it)) {
            Column(modifier = Modifier.fillMaxWidth().wrapContentHeight(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center){
                gamerViewModel.gamerList.value.forEachIndexed { index, gamer ->
                    //Text("Oyuncu ${index+1}")
                    Text("++++")
                    Text(gamer.name)
                }
            }
        }
    }
}