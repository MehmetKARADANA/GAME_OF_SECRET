package com.mobile.gameofsecret.ui.components

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mobile.gameofsecret.ui.theme.textColor
import com.mobile.gameofsecret.ui.theme.toolbar

@Composable
fun Header() {
    val statusBarColor = toolbar
    val activity = LocalActivity.current
    activity?.window?.statusBarColor = statusBarColor.toArgb()
Card(modifier = Modifier.wrapContentSize(),
    shape = RoundedCornerShape(0.dp),
    elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(color = toolbar),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Deneme",
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.W500,
            fontSize = 22.sp,
            color = textColor
        )
    }}
}