package com.mobile.gameofsecret.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mobile.gameofsecret.DestinationScreen
import com.mobile.gameofsecret.R
import com.mobile.gameofsecret.data.model.Language
import com.mobile.gameofsecret.localization.LanguageManager
import com.mobile.gameofsecret.localization.changeLocale
import com.mobile.gameofsecret.ui.components.BackHeader
import com.mobile.gameofsecret.ui.components.LanguageRow
import com.mobile.gameofsecret.ui.components.LargeButton
import com.mobile.gameofsecret.ui.theme.background
import com.mobile.gameofsecret.ui.theme.buttonColors1
import com.mobile.gameofsecret.ui.utils.navigateTo

@Composable
fun LanguageScreen(navController: NavController) {
    val context = LocalContext.current
    var currentLanguage by remember { mutableStateOf(LanguageManager.getLanguage(context)) }
    val languages = listOf(
        Language("Türkçe", R.drawable.turkey, "tr"),
        Language("English", R.drawable.en_flag, "en"),
        Language("Français", R.drawable.fr_flag, "fr")
    )

    Scaffold(modifier = Modifier, topBar = {
        BackHeader(onBackClicked = {
            navController.popBackStack()
        }, headerText = stringResource(R.string.language))
    }) {
        Box(modifier = Modifier.padding(it)) {
            Column(
                modifier = Modifier
                    .background(background)
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Welcome Text
                Text(
                    text = context.resources.getString(R.string.app_name),
                    modifier = Modifier.padding(bottom = 16.dp), color = Color.White
                )

                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    item {
                        languages.forEach { language ->
                            Column(
                                modifier = Modifier
                                    .wrapContentSize()
                                    .padding(top=8.dp)
                            ) {
                                LanguageRow(language, isSelected =
                                language.label == currentLanguage, onClick = {
                                    changeLocale(context, language.label)
                                    currentLanguage = language.label
                                })
                            }
                        }
                    }
                }

            }
        }
    }
}