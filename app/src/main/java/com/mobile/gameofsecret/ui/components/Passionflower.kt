package com.mobile.gameofsecret.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberBasicTooltipState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun WheelOfFortuneScreen() {
    val sections = remember {
        listOf(
            WheelSection("500 TL", Color(0xFFE53935)),
            WheelSection("200 TL", Color(0xFF43A047)),
            WheelSection("1000 TL", Color(0xFF1E88E5)),
            WheelSection("0 TL", Color(0xFFFFB300)),
            WheelSection("100 TL", Color(0xFF8E24AA)),
            WheelSection("300 TL", Color(0xFF00897B)),
            WheelSection("50 TL", Color(0xFFD81B60)),
            WheelSection("400 TL", Color(0xFF7CB342))
        )
    }

    var spinning by remember { mutableStateOf(false) }
    var result by remember { mutableStateOf("Çevirmeye başla!") }
    var rotationAngle by remember { mutableStateOf(0f) }

    // Animasyonun dönüş açısını hesaplamak için infiniteTransition
    val infiniteTransition = rememberInfiniteTransition(label = "wheel_rotation")
    val animatedRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
        ),
        label = "wheel_rotation"
    )

    // Çarkın toplam dönüş açısı
    val angle = if (spinning) animatedRotation + rotationAngle else rotationAngle

    // Dilim büyüklüğü
    val sectionSize = 360f / sections.size

    // Animasyonun bitiminden sonra, hangi dilimde olduğumuzu hesaplamak
    val visibleResult = (angle % 360f).let { angleInRange ->
        val sectionIndex = ((angleInRange / sectionSize) + sections.size) % sections.size
        sections[(sections.size - 1 - sectionIndex).toInt()].label
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Sonucu göstermek
        Text(
            text = "Sonuç: $visibleResult",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 24.dp)
        )

        // Çarkın render edildiği Box
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            WheelOfFortune(
                sections = sections,
                rotationAngle = angle
            )
        }

        // Çarkı döndürmek için buton
        Button(
            onClick = {
                if (!spinning) {
                    spinning = true
                    // Random bir dönüş miktarı
                    val randomSpin = (Random.nextInt(2, 6) * 360) +
                            (Random.nextInt(0, sections.size) * (360 / sections.size)) +
                            Random.nextInt(0, 360 / sections.size)

                    // Toplam dönüş açısını güncelle
                    rotationAngle += randomSpin.toFloat()

                    // Çarkı durdurduktan sonra sonucu hesapla
                    kotlinx.coroutines.MainScope().launch {
                        kotlinx.coroutines.delay(2000) // 3 saniye bekle (animasyon süresi)
                        spinning = false

                        // Sonuç dilimini hesapla
                        val normalizedAngle = (rotationAngle % 360).toInt()
                        val sectionIndex = ((normalizedAngle / sectionSize) + sections.size) % sections.size
                        result = "Sonuç: ${sections[(sections.size - 1 - sectionIndex).toInt()].label}"
                    }
                }
            },
            enabled = !spinning,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = if (spinning) "Dönüyor..." else "Çarkı Çevir!", fontSize = 18.sp)
        }
    }
}



@Composable
fun WheelOfFortune(
    sections: List<WheelSection>,
    rotationAngle: Float,
    modifier: Modifier = Modifier
) {
    val textMeasurer = rememberTextMeasurer()

    Canvas(
        modifier = modifier
            .aspectRatio(1f)
            .padding(8.dp)
    ) {
        val center = Offset(size.width / 2, size.height / 2)
        val radius = size.minDimension / 2
        val sectionAngle = 360f / sections.size

        // Draw the wheel with sections
        rotate(rotationAngle) {
            sections.forEachIndexed { index, section ->
                val startAngle = index * sectionAngle

                // Draw section
                drawArc(
                    color = section.color,
                    startAngle = startAngle,
                    sweepAngle = sectionAngle,
                    useCenter = true,
                    topLeft = Offset(center.x - radius, center.y - radius),
                    size = Size(radius * 2, radius * 2)
                )

                // Draw section labels - improved positioning
                val midAngle = Math.toRadians((startAngle + sectionAngle / 2).toDouble())
                val labelRadius = radius * 0.65f // Adjust this value to position text within the arc
                val x = center.x + (cos(midAngle) * labelRadius).toFloat()
                val y = center.y + (sin(midAngle) * labelRadius).toFloat()

                // Rotate text to be radial (from center outward)
                rotate(
                    degrees = startAngle + sectionAngle / 2 + 90,
                    pivot = Offset(x, y)
                ) {
                    val textLayoutResult = textMeasurer.measure(
                        text = section.label,
                        style = TextStyle(
                            fontSize = 16.sp, // Increase font size for better visibility
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    )

                    // Center the text properly
                    drawText(
                        textLayoutResult = textLayoutResult,
                        topLeft = Offset(
                            x - textLayoutResult.size.width / 2,
                            y - textLayoutResult.size.height / 2
                        )
                    )
                }
            }
        }

        // Draw outer circle
        drawCircle(
            color = Color.Black,
            radius = radius,
            center = center,
            style = Stroke(width = 8f)
        )

        // Draw center circle
        drawCircle(
            color = Color.White,
            radius = radius * 0.1f,
            center = center
        )

        val pointerPath = androidx.compose.ui.graphics.Path().apply {
            moveTo(center.x, center.y - radius + 16) // Üst kısmın tam ortası (daha yukarıda)
            lineTo(center.x - 20, center.y - radius - 20) // Sol üst nokta
            lineTo(center.x + 20, center.y - radius - 20) // Sağ üst nokta
            close() // Üçgeni kapat
        }


        drawPath(
            path = pointerPath,
            color = Color.Red
        )
    }
}

data class WheelSection(
    val label: String,
    val color: Color
)