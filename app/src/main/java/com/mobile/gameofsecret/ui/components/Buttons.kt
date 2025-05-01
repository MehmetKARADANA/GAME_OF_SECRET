package com.mobile.gameofsecret.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mobile.gameofsecret.ui.theme.buttonColors1

@Composable
fun LargeButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = { onClick.invoke() },
        modifier = Modifier.fillMaxWidth(),
        elevation = ButtonDefaults.elevatedButtonElevation(12.dp),
        shape = RoundedCornerShape(12.dp),
        colors = buttonColors1
    ) {
        Text(text = text)
    }
}

@Composable
fun ButtonText(text: String, onClick: () -> Unit) {
    Button(
        onClick = { onClick.invoke() }, elevation = ButtonDefaults.elevatedButtonElevation(12.dp),
        modifier = Modifier.wrapContentSize(),
        shape = RoundedCornerShape(12.dp),
        colors = buttonColors1
    ) {
        Text(text = text)
    }
}