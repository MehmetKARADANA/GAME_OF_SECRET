package com.mobile.gameofsecret.ui.theme

import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)
//val background =Color(0xFFFFA000)
val background = Color(0xFF333399)
val toolbar = Color(0x3E0039FF)
val textColor = Color(0xFFFFFFFF)
val buttonColors1 = ButtonColors(
    containerColor = Color.White, // 🔹 Siyah arka plan
    contentColor = Color.Black,
    disabledContainerColor = Color.Transparent,
    disabledContentColor = Color.Gray    // 🔹 Beyaz yazı
)

val sectorColors = listOf(
    Color(0xFFFF6B6B),   // Parlak Kırmızı
    Color(0xFF4ECDC4),   // Turkuaz
    Color(0xFF45B7D1),   // Açık Mavi
    Color(0xFFFFD93D),   // Sarı
    Color(0xFFF8A5C2),   // Pembe
    Color(0xFF6A5ACD),   // Mor
    Color(0xFF7ED6DF),   // Açık Turkuaz
    Color(0xFFFFA07A)    // Mercan
)

val cardcolor = CardColors(
    containerColor = Color(0xFF333399), // Koyu mor-mavi arka plan
    contentColor = Color.White, // Beyaz yazılar (kontrast için)
    disabledContentColor = Color(0xFFB7A8C7), // Grileşmiş beyaz içerik (disabled)
    disabledContainerColor = Color(0xFF6666B3) // Koyu mor-mavi tonunun daha açık hali (disabled)
)

val cardcolor2 = CardColors(
    containerColor = Color.White, // Koyu mor-mavi arka plan
    contentColor = Color.Black, // Beyaz yazılar (kontrast için)
    disabledContentColor = Color(0xFFB7A8C7), // Grileşmiş beyaz içerik (disabled)
    disabledContainerColor = Color.White // Koyu mor-mavi tonunun daha açık hali (disabled)
)

val cardcolor3 = CardColors(
    containerColor = Color(0xFF2196F3), // Koyu mor-mavi arka plan
    contentColor = Color.White, // Beyaz yazılar (kontrast için)
    disabledContentColor = Color(0xFFB7A8C7), // Grileşmiş beyaz içerik (disabled)
    disabledContainerColor = Color(0xFF2196F3) // Koyu mor-mavi tonunun daha açık hali (disabled)
)

val cardcolor4 = CardColors(
    containerColor = Color(0xFF333399), // Koyu mor-mavi arka plan
    contentColor = Color.White, // Beyaz yazılar (kontrast için)
    disabledContentColor = Color(0xFFB7A8C7), // Grileşmiş beyaz içerik (disabled)
    disabledContainerColor = Color(0xFF6666B3) // Koyu mor-mavi tonunun daha açık hali (disabled)
)
@Composable
fun textFieldColor() = TextFieldDefaults.colors(
    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White,
    disabledTextColor = Color.Gray,
    focusedContainerColor = Color.Transparent,
    unfocusedContainerColor = Color.Transparent,
    disabledContainerColor = Color.Transparent,
    focusedIndicatorColor = Color.White,
    unfocusedIndicatorColor = Color.Gray,
    disabledIndicatorColor = Color.Transparent,
    cursorColor = Color.White, // Tamamen şeffaf bir imleç kullanıcı için kullanışsız olabilir
    focusedLabelColor = Color.White,
    unfocusedLabelColor = Color.White
)

