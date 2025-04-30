package com.mobile.gameofsecret.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mobile.gameofsecret.R
import com.mobile.gameofsecret.localization.changeLocale

@Composable
fun LanguageScreen() {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Welcome Text
        Text(
            text = context.resources.getString(R.string.app_name),
            modifier = Modifier.padding(bottom = 16.dp)
        )
        // Language Selection
        Text(
            text = context.resources.getString(R.string.select_language),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = { changeLocale(context, "en") }) {
                    Text(text = "English")
                }
                Button(onClick = { changeLocale(context, "fr") }) {
                    Text(text = "Français")
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