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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random


data class WheelSection(
    val label: String,
    val color: Color
)

@Composable
fun EmptyWheelOfFortune(
    sections: List<WheelSection>,
    rotationAngle: Float,
    modifier: Modifier = Modifier
) {
    val textMeasurer = rememberTextMeasurer()

    Canvas(
        modifier = modifier
            .aspectRatio(1f)
            .padding(48.dp)
    ) {
        val center = Offset(size.width / 2, size.height / 2)
        val radius = size.minDimension / 2
        val sectionAngle = 360f / sections.size

        // Çarkın bölümlerini çiz
        rotate(rotationAngle) {
            sections.forEachIndexed { index, section ->
                val startAngle = index * sectionAngle

                // Bölüm çizimi
                drawArc(
                    color = section.color,
                    startAngle = startAngle,
                    sweepAngle = sectionAngle,
                    useCenter = true,
                    topLeft = Offset(center.x - radius, center.y - radius),
                    size = Size(radius * 2, radius * 2)
                )

                // Bölüm etiketlerini çiz - metin pozisyonlandırması
                val midAngle = Math.toRadians((startAngle + sectionAngle / 2).toDouble())
                val labelRadius = radius * 0.65f // Metni yay üzerinde düzgün yerleştirmek için
                val x = center.x + (cos(midAngle) * labelRadius).toFloat()
                val y = center.y + (sin(midAngle) * labelRadius).toFloat()
/*
                // Metni radial şekilde döndür (merkezden dışarıya doğru)
                rotate(
                    degrees = startAngle + sectionAngle / 2 + 90,
                    pivot = Offset(x, y)
                ) {
                    val textLayoutResult = textMeasurer.measure(
                        text = section.label,
                        style = TextStyle(
                            fontSize = 16.sp, // Font büyüklüğünü artırarak okunabilirliği artır
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    )*/

                    /*// Metni düzgün şekilde ortalamak
                    drawText(
                        textLayoutResult = textLayoutResult,
                        topLeft = Offset(
                            x - textLayoutResult.size.width / 2,
                            y - textLayoutResult.size.height / 2
                        )
                    )
                }*/
            }
        }

        // Dış çerçeveyi çiz
        drawCircle(
            color = Color.Black,
            radius = radius,
            center = center,
            style = Stroke(width = 12f)
        )

        // İç çemberi çiz
        drawCircle(
            color = Color.White,
            radius = radius * 0.1f,
            center = center
        )

        // Çarkın üstünde bir ok (göstergesi)
        val pointerPath = androidx.compose.ui.graphics.Path().apply {
            moveTo(center.x, center.y - radius + 16) // Üst kısmın tam ortası
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
            .padding(48.dp)
    ) {
        val center = Offset(size.width / 2, size.height / 2)
        val radius = size.minDimension / 2
        val sectionAngle = 360f / sections.size

        // Çarkın bölümlerini çiz
        rotate(rotationAngle) {
            sections.forEachIndexed { index, section ->
                val startAngle = index * sectionAngle

                // Bölüm çizimi
                drawArc(
                    color = section.color,
                    startAngle = startAngle,
                    sweepAngle = sectionAngle,
                    useCenter = true,
                    topLeft = Offset(center.x - radius, center.y - radius),
                    size = Size(radius * 2, radius * 2)
                )

                // Bölüm etiketlerini çiz - metin pozisyonlandırması
                val midAngle = Math.toRadians((startAngle + sectionAngle / 2).toDouble())
                val labelRadius = radius * 0.65f // Metni yay üzerinde düzgün yerleştirmek için
                val x = center.x + (cos(midAngle) * labelRadius).toFloat()
                val y = center.y + (sin(midAngle) * labelRadius).toFloat()
                /*
                                // Metni radial şekilde döndür (merkezden dışarıya doğru)
                                rotate(
                                    degrees = startAngle + sectionAngle / 2 + 90,
                                    pivot = Offset(x, y)
                                ) {
                                    val textLayoutResult = textMeasurer.measure(
                                        text = section.label,
                                        style = TextStyle(
                                            fontSize = 16.sp, // Font büyüklüğünü artırarak okunabilirliği artır
                                            color = Color.White,
                                            textAlign = TextAlign.Center
                                        )
                                    )*/

                /*// Metni düzgün şekilde ortalamak
                drawText(
                    textLayoutResult = textLayoutResult,
                    topLeft = Offset(
                        x - textLayoutResult.size.width / 2,
                        y - textLayoutResult.size.height / 2
                    )
                )
            }*/
            }
        }

        // Dış çerçeveyi çiz
        drawCircle(
            color = Color.Black,
            radius = radius,
            center = center,
            style = Stroke(width = 12f)
        )

        // İç çemberi çiz
        drawCircle(
            color = Color.White,
            radius = radius * 0.1f,
            center = center
        )

        // Çarkın üstünde bir ok (göstergesi)
        val pointerPath = androidx.compose.ui.graphics.Path().apply {
            moveTo(center.x, center.y - radius + 16) // Üst kısmın tam ortası
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
