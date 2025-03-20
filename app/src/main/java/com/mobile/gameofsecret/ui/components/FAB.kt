package com.mobile.gameofsecret.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FAB(onClick: () -> Unit,text : String) {
    ExtendedFloatingActionButton(
        onClick = { onClick.invoke() }, contentColor = Color.Black ,
        containerColor = Color.White,
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
    ) {
        Text(text, fontSize = 16.sp)
    }
}