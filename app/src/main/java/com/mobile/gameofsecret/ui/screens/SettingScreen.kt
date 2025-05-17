package com.mobile.gameofsecret.ui.screens

import android.provider.Settings
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mobile.gameofsecret.DestinationScreen
import com.mobile.gameofsecret.R
import com.mobile.gameofsecret.data.AdId
import com.mobile.gameofsecret.ui.components.BackHeader
import com.mobile.gameofsecret.ui.components.BannerAdCard
import com.mobile.gameofsecret.ui.components.ButtonText
import com.mobile.gameofsecret.ui.components.LargeButton
import com.mobile.gameofsecret.ui.theme.background
import com.mobile.gameofsecret.ui.theme.buttonColors1
import com.mobile.gameofsecret.ui.theme.switchColor
import com.mobile.gameofsecret.ui.utils.ObserveErrorMessage
import com.mobile.gameofsecret.ui.utils.getSettingName
import com.mobile.gameofsecret.ui.utils.navigateTo
import com.mobile.gameofsecret.viewmodels.NotificationViewModel
import com.mobile.gameofsecret.viewmodels.SettingsViewModel

enum class Items(val setting: String, val route: String) {
    LANGUAGE(setting = "LANGUAGE", route = DestinationScreen.Languages.route),
    TERMS(setting = "TERMS", route = DestinationScreen.Terms.route),
    PRIVACY(setting = "PRIVACY", route = DestinationScreen.Privacy.route),
    ABOUT_US(setting = "ABOUT US", route = DestinationScreen.AboutUs.route)
}


@Composable
fun SettingScreen(
    navController: NavController,
    settingsViewModel: SettingsViewModel,
    notificationViewModel: NotificationViewModel
) {
    val errorMessage by notificationViewModel.errorMessage
    val context = LocalContext.current
    ObserveErrorMessage(errorMessage)

    val isNotificationEnabled by settingsViewModel.isNotificationEnabled.collectAsState()
    val isFirstLaunch = settingsViewModel.isFirstLaunch

    BackHandler {
        navigateTo(navController,DestinationScreen.Menu.route)
    }

    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        BackHeader(onBackClicked = {
            navigateTo(navController,DestinationScreen.Menu.route)
        }, headerText = stringResource(R.string.settings))
    }) { it ->
        Column(modifier = Modifier.background(background).padding(it)) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(background)
                        .weight(1f)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("\uD83D\uDD14 "+ stringResource(R.string.notifications), color = Color.White)
                        Spacer(modifier = Modifier.padding(8.dp))

                        Switch(
                            checked = isNotificationEnabled,
                            onCheckedChange = {
                                settingsViewModel.setNotificationEnabled(
                                    "gos",
                                    it
                                )
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = switchColor,
                                uncheckedThumbColor = Color.Gray,
                                uncheckedTrackColor = Color.DarkGray
                            ),
                        )
                    }

                    Spacer(modifier = Modifier.padding(8.dp))
                    ButtonText(text =stringResource(R.string.notification_settings) ) { notificationViewModel.openNotificationSettings(context) }

                    Spacer(modifier = Modifier.padding(16.dp))
                    Text(
                        "ℹ\uFE0F "+ stringResource(R.string.notification_info),
                        modifier = Modifier.padding(8.dp), fontSize = 12.sp, color = Color.Gray
                    )
                    Spacer(modifier = Modifier.padding(16.dp))
                    LazyColumn {
                        items(Items.entries) {
                            LargeButton(text = getSettingName(it)) {
                                navigateTo(navController, route = it.route)
                            }
                        }
                    }

                }
            BannerAdCard(adUnitId = AdId)
            Spacer(modifier = Modifier.height(5.dp))
        }
    }
}