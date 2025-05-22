package com.mobile.gameofsecret.ui.components

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mobile.gameofsecret.ui.theme.background

@Composable
fun BackHeader(onBackClicked: () -> Unit, headerText: String) {
    val statusBarColor = background
    val activity = LocalActivity.current
    activity?.window?.statusBarColor = statusBarColor.toArgb()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(color = background),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            Icons.Default.ArrowBack, contentDescription = "Back",
            modifier = Modifier
                .padding(start = 8.dp)
                .size(30.dp)
                //.shadow(24.dp, shape = RoundedCornerShape(12.dp), clip = false)
                .clickable {
                    onBackClicked.invoke()
                }, tint = Color.White
        )
        Text(
            text = headerText,
            fontWeight = FontWeight.W500,
            //fontFamily = FontFamily.Serif,
            fontSize = 22.sp,
            modifier = Modifier
                .padding(bottom = 10.dp, top = 10.dp)
               // .shadow(24.dp, shape = RoundedCornerShape(12.dp), clip = false),
           , color = Color.White
        )
        Spacer(modifier = Modifier.padding(30.dp))
    }
}