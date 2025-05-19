package com.mobile.gameofsecret.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mobile.gameofsecret.R
import com.mobile.gameofsecret.data.MAIL
import com.mobile.gameofsecret.ui.components.BackHeader
import com.mobile.gameofsecret.ui.theme.background
import com.mobile.gameofsecret.ui.utils.navigateTo

@Composable
fun AboutUsScreen(navController: NavController) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
            .padding(WindowInsets.systemBars.asPaddingValues()),
        topBar = {
            BackHeader(
                headerText = stringResource(R.string.about_us), onBackClicked = {
                    navController.popBackStack()
                }
            )
        }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(background)
                .padding(it)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(background)
                    .padding(16.dp)
            ) {
                item {
                    // About Us Title (Bold)
                    Text(
                        text = stringResource(R.string.about_us),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold, // Bold title
                        modifier = Modifier.padding(bottom = 8.dp),
                        color = Color.White
                    )
                }
                item {
                    Text(
                        stringResource(R.string.about_game_description),
                        color = Color.White,
                        textAlign = TextAlign.Left
                    )
                }
                item {
                    SelectionContainer {
                        Text(
                            stringResource(R.string.contact) + ": " + MAIL,
                            modifier = Modifier.padding(bottom = 8.dp),
                            color = Color.White,
                            textAlign = TextAlign.Justify
                        )
                    }
                }
            }
        }

    }
}