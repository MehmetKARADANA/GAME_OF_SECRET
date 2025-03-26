package com.mobile.gameofsecret.ui.components

import android.graphics.Paint
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mobile.gameofsecret.R
import com.mobile.gameofsecret.ui.theme.background
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun TruthDareBottleGame() {
    val players = listOf("Mehmet", "Ayşe", "Fatma", "Ahmet", "Burak", "Zeynep")
    val playerCount = players.size
    val playerAngle = 360f / playerCount // Oyuncuların açı dağılımı

    var rotationAngle by remember { mutableStateOf(0f) }
    var asker by remember { mutableStateOf("") }
    var sorulan by remember { mutableStateOf("") }
    var showResult by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope() // Yeni CoroutineScope oluştur

    val animatedRotation by animateFloatAsState(
        targetValue = rotationAngle,
        animationSpec = tween(durationMillis = 4000, easing = FastOutSlowInEasing),
        label = "Bottle Rotation"
    )

    Column(
        modifier = Modifier.fillMaxSize().background(background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier.size(300.dp),
            contentAlignment = Alignment.Center
        ) {
            // Oyuncuları yerleştir
            Canvas(modifier = Modifier.fillMaxSize()) {
                val radius = size.minDimension / 2.5f
                players.forEachIndexed { index, name ->
                    val angle = index * playerAngle
                    val radians = Math.toRadians(angle.toDouble())
                    val x = (size.width / 2 + radius * cos(radians)).toFloat()
                    val y = (size.height / 2 + radius * sin(radians)).toFloat()

                    drawContext.canvas.nativeCanvas.drawText(
                        name,
                        x,
                        y,
                        Paint().apply {
                            textSize = 40f
                            textAlign = Paint.Align.CENTER
                            color = android.graphics.Color.WHITE
                        }
                    )
                }
            }

            // Şişe Görseli
            Image(
                painter = painterResource(id = R.drawable.bottle),
                contentDescription = "Spinning Bottle",
                modifier = Modifier
                    .size(150.dp)
                    .rotate(animatedRotation)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {
            showResult = false // Sonucu sıfırla

            val extraRotations = (5..8).random() * 360 // En az 5 tur dönsün
            val newAngle = extraRotations + (0..360).random()

            rotationAngle = newAngle.toFloat() // Yeni açıyı animasyona aktar

            // Sonucu hesaplamak için gecikmeli işlem
            scope.launch {
                delay(4000) // Animasyon süresi kadar bekle

                // Şişenin döndürülmesi ile ilgili açıyı doğru hesapla
                val frontIndex = (((rotationAngle % 360) / playerAngle).toInt()) % playerCount
                val backIndex = (frontIndex + playerCount / 2) % playerCount

                sorulan = players[frontIndex]  // Şişenin önü
                asker = players[backIndex]    // Şişenin arkası
                showResult = true // Sonucu göster
            }
        }) {
            Text(text = "Şişeyi Döndür")
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (showResult) {
            Text(
                text = "🎤 $asker → $sorulan'a soru soruyor!",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
