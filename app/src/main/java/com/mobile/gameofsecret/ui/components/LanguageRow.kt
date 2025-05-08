package com.mobile.gameofsecret.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mobile.gameofsecret.data.model.Language
import com.mobile.gameofsecret.ui.theme.switchColor

@Composable
fun LanguageRow(
    language: Language,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) Color(0xFFFFF8DC) else Color.White
    val borderColor = if (isSelected) Color.Cyan else Color.Transparent

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .border(BorderStroke(1.dp, borderColor), shape = RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = language.image),
            contentDescription = language.name,
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = language.name,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Black
        )
        Spacer(modifier = Modifier.weight(1f))
        if (isSelected) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "selected Language",
                tint = switchColor
            )
        }
    }
}
