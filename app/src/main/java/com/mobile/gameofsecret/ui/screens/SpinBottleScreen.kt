package com.mobile.gameofsecret.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.mobile.gameofsecret.ui.components.FAB
import com.mobile.gameofsecret.ui.components.TruthDareBottleGame
import com.mobile.gameofsecret.ui.theme.background
import com.mobile.gameofsecret.viewmodels.QuizViewModel

@Composable
fun SpinBottleScreen(quizViewModel: QuizViewModel) {
/*
    val question by quizViewModel.question.collectAsState()
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(background), floatingActionButton = {
                FAB(onClick = {
                    quizViewModel.getRandomQuestion()
                }, text = "soru al")
        }, floatingActionButtonPosition = FabPosition.Center
    ) {
        Surface(modifier = Modifier
            .fillMaxSize()
            .background(background)
            .padding(it)) {
           // TruthDareBottleGame()
            Text(text = question.toString())


        }
    }*/
}