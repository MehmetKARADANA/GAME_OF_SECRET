package com.mobile.gameofsecret.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mobile.gameofsecret.DestinationScreen
import com.mobile.gameofsecret.R
import com.mobile.gameofsecret.localization.LanguageManager
import com.mobile.gameofsecret.localization.changeLocale
import com.mobile.gameofsecret.ui.components.LargeButton
import com.mobile.gameofsecret.ui.theme.background
import com.mobile.gameofsecret.ui.theme.buttonColors1
import com.mobile.gameofsecret.ui.utils.navigateTo

@Composable
fun LanguageScreen() {
    val context = LocalContext.current
    var currentLanguage by remember { mutableStateOf(LanguageManager.getLanguage(context)) }

    Column(
        modifier = Modifier.background(background)
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Welcome Text
        Text(
            text = context.resources.getString(R.string.app_name),
            modifier = Modifier.padding(bottom = 16.dp), color = Color.White
        )
        // Language Selection
        Text(
            text = context.resources.getString(R.string.select_language),
            modifier = Modifier.padding(bottom = 8.dp),
            color = Color.White
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                LargeButton(text = "Türkçe") {
                    changeLocale(context, "tr")
                    currentLanguage="tr"
                }
                LargeButton(text = "English") {
                    changeLocale(context, "en")
                    currentLanguage="en"
                }
                LargeButton(text = "Français") {
                    changeLocale(context, "fr")
                    currentLanguage="fr"
                }
            }

        }
        // Flag Image
        /* Image(
             painter = painterResource(id = R.drawable.flag),
             contentDescription = null,
             modifier = Modifier
                 .padding(top = 32.dp)
                 .size(200.dp)
         )*/
    }
}