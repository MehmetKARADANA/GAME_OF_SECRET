package com.mobile.gameofsecret.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.mobile.gameofsecret.ui.components.BackHeader
import com.mobile.gameofsecret.ui.theme.background
import com.mobile.gameofsecret.ui.utils.ObserveErrorMessage
import com.mobile.gameofsecret.viewmodels.NotificationViewModel
import com.mobile.gameofsecret.viewmodels.SettingsViewModel

@Composable
fun SettingScreen(navController: NavController,settingsViewModel: SettingsViewModel,notificationViewModel: NotificationViewModel) {
    val errorMessage by notificationViewModel.errorMessage
    val context = LocalContext.current
    ObserveErrorMessage(errorMessage)

    val isChatNotificationEnabled by settingsViewModel.isChatNotificationEnabled.collectAsState()
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
                }, "Bildirim Ayarları")
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("\uD83D\uDD14 Duyuru Bildirimleri", color = Color.White)
                        Spacer(modifier = Modifier.padding(8.dp))

                        Switch(
                            checked = isChatNotificationEnabled,
                            onCheckedChange = { settingsViewModel.setNotificationEnabled("gos", it) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White, // Açıkken düğme rengi
                                checkedTrackColor = Color.Cyan, // Açıkken arka plan
                                uncheckedThumbColor = Color.Gray, // Kapalıyken düğme rengi
                                uncheckedTrackColor = Color.DarkGray // Kapalıyken arka plan
                            ),
                        )
                    }

                    Spacer(modifier = Modifier.padding(8.dp))
                    Button(
                        onClick = { notificationViewModel.openNotificationSettings(context) },
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
                }
            }
        }
    }
}