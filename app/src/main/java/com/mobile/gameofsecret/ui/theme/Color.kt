package com.mobile.gameofsecret.ui.theme

import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.mobile.gameofsecret.ui.components.WheelSection

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
val switchColor = Color(0xFF007AFF)
val buttonColors1 = ButtonColors(
    containerColor = Color.White,
    contentColor = Color.Black,
    disabledContainerColor = Color.Transparent,
    disabledContentColor = Color.Gray
)
val buttonColors2 = ButtonColors(
    containerColor = Color(0xFF2196F3), // Koyu mor-mavi arka plan
    contentColor = Color.White, // Beyaz yazılar (kontrast için)
    disabledContentColor = Color(0xFFB7A8C7), // Grileşmiş beyaz içerik (disabled)
    disabledContainerColor = Color(0xFF2196F3) // Koyu m
)
val circleColor =Color(0xFF3A3A55)
    //Color(0xFF3F51B5)
    //Color(0xFF3A3A55)
val sectorColors = listOf(
    Color(0xFFFFB300),
    Color(0xFF8E24AA),
    Color(0xFFE53935),
    Color(0xFF1E88E5),
    Color(0xFF43A047),
    Color(0xFF00897B),
        Color(0xFFF4511E),
        Color(0xFF3949AB),
        Color(0xFF00ACC1),
        Color(0xFF7CB342),
        Color(0xFFFF7043),
        Color(0xFF6D4C41),
        Color(0xFF5E35B1),
        Color(0xFF039BE5),
        Color(0xFFEC407A),

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


