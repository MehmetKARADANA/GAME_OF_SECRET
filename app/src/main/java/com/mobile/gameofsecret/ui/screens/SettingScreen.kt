package com.mobile.gameofsecret.ui.screens

import android.provider.Settings
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mobile.gameofsecret.DestinationScreen
import com.mobile.gameofsecret.ui.components.BackHeader
import com.mobile.gameofsecret.ui.theme.background
import com.mobile.gameofsecret.ui.theme.buttonColors1
import com.mobile.gameofsecret.ui.utils.ObserveErrorMessage
import com.mobile.gameofsecret.viewmodels.NotificationViewModel
import com.mobile.gameofsecret.viewmodels.SettingsViewModel

enum class items(val setting: String, val route: String) {
    TERMS(setting = "TERMS", route ="" ),
    PRIVACY(setting = "PRIVACY", route = DestinationScreen.SerialGame.route),
    ABOUTUS(setting = "ABOUTUS", route = DestinationScreen.SpinBottle.route)
}


@Composable
fun SettingScreen(navController: NavController,settingsViewModel: SettingsViewModel,notificationViewModel: NotificationViewModel) {
    val errorMessage by notificationViewModel.errorMessage
    val context = LocalContext.current
    ObserveErrorMessage(errorMessage)

    val isNotificationEnabled by settingsViewModel.isNotificationEnabled.collectAsState()
    val isFirstLaunch = settingsViewModel.isFirstLaunch

    BackHandler {
        navController.popBackStack()
    }

    Scaffold(modifier = Modifier.fillMaxSize()) {
        Surface(modifier = Modifier.padding(it)) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(background)
            ) {
                BackHeader(onBackClicked = {
                    navController.popBackStack()
                }, "Ayarlar")
                Column (
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    //verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("\uD83D\uDD14 Bildirimler", color = Color.White)
                        Spacer(modifier = Modifier.padding(8.dp))

                        Switch(
                            checked = isNotificationEnabled,
                            onCheckedChange = { settingsViewModel.setNotificationEnabled("gos", it) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = Color.Cyan,
                                uncheckedThumbColor = Color.Gray,
                                uncheckedTrackColor = Color.DarkGray
                            ),
                        )
                    }

                    Spacer(modifier = Modifier.padding(8.dp))
                    Button(
                        onClick = { notificationViewModel.openNotificationSettings(context) },
                        colors = buttonColors1,
                      /*  colors = buttonColor(),*/
                        //elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 16.dp)
                    ) {
                        Text("Bildirim Ayarları")
                    }

                    Spacer(modifier = Modifier.padding(16.dp))
                    Text(
                        "ℹ\uFE0F Bildirimleriniz açık olmasına rağmen herhangi bir bildirim almıyorsanız, cihazınızın bildirim ayarlarını kontrol etmek için Bildirim Ayarları sekmesini ziyaret edebilirsiniz.",
                        modifier = Modifier.padding(8.dp), fontSize = 12.sp, color = Color.Gray
                    )
                    Spacer(modifier = Modifier.padding(16.dp))
                    LazyColumn {
                       // items(Se) {

                      //  }
                    }

                }
            }
        }
    }
}