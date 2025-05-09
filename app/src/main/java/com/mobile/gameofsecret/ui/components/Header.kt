package com.mobile.gameofsecret.ui.components

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mobile.gameofsecret.DestinationScreen
import com.mobile.gameofsecret.R
import com.mobile.gameofsecret.ui.theme.background
import com.mobile.gameofsecret.ui.theme.textColor
import com.mobile.gameofsecret.ui.utils.navigateTo

@Composable
fun Header(navController: NavController,header: String) {
    //burası appin tamamı için statusbar
    val statusBarColor = background
    val activity = LocalActivity.current
    activity?.window?.statusBarColor = statusBarColor.toArgb()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(color = background),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(36.dp))
       /* Icon(
            painter = painterResource(R.drawable.information),
            contentDescription = "information",
            tint = Color.White,
            modifier = Modifier
                .padding(start = 12.dp)
                .size(24.dp)
                .clickable {
                    navigateTo(
                        navController = navController,
                        route = DestinationScreen.Info.route
                    )
                }
        )*/
        Text(
            text = header,
           // fontFamily = FontFamily.Default,
            fontWeight = FontWeight.W400,
            fontSize = 22.sp,
            color = textColor
        )
        Icon(
            painter = painterResource(R.drawable.setting),
            contentDescription = "Settings",
            tint = Color.White,
            modifier = Modifier
                .padding(end = 12.dp)
                .size(24.dp)
                .clickable {
                    navigateTo(
                        navController = navController,
                        route = DestinationScreen.Settings.route
                    )
                }
        )
    }
}


@Composable
fun PreHeader(navController: NavController,header : String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(color = background),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Default.ArrowBack, contentDescription = "Back",
            modifier = Modifier
                .padding(start = 8.dp)
                .size(30.dp)
                //.shadow(24.dp, shape = RoundedCornerShape(12.dp), clip = false)
                .clickable {
                  navController.popBackStack()
                }, tint = Color.White
        )
        Text(
            text = header,
           // fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.W400,
            fontSize = 22.sp,
            color = textColor
        )
        Icon(
            painter = painterResource(R.drawable.setting),
            contentDescription = "Settings",
            tint = Color.White,
            modifier = Modifier
                .padding(end = 12.dp)
                .size(24.dp)
                .clickable {
                    navigateTo(
                        navController = navController,
                        route = DestinationScreen.Settings.route
                    )
                }
        )
    }
}