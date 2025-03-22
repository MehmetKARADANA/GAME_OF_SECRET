package com.mobile.gameofsecret.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

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
            .padding(64.dp)
    ) {
        val center = Offset(size.width / 2, size.height / 2)
        val radius = size.minDimension / 2
        val sectionAngle = 360f / sections.size

        rotate(rotationAngle) {
            sections.forEachIndexed { index, section ->
                val startAngle = index * sectionAngle

                drawArc(
                    color = section.color,
                    startAngle = startAngle,
                    sweepAngle = sectionAngle,
                    useCenter = true,
                    topLeft = Offset(center.x - radius, center.y - radius),
                    size = Size(radius * 2, radius * 2)
                )

                val midAngle = Math.toRadians((startAngle + sectionAngle / 2).toDouble())
                val labelRadius = radius * 0.65f
                val x = center.x + (cos(midAngle) * labelRadius).toFloat()
                val y = center.y + (sin(midAngle) * labelRadius).toFloat()

            }
        }

        drawCircle(
            color = Color.Black,
            radius = radius,
            center = center,
            style = Stroke(width = 12f)
        )

        drawCircle(
            color = Color.White,
            radius = radius * 0.1f,
            center = center
        )

        val pointerPath = androidx.compose.ui.graphics.Path().apply {
            moveTo(center.x, center.y - radius + 16)
            lineTo(center.x - 20, center.y - radius - 20)
            lineTo(center.x + 20, center.y - radius - 20)
            close()
        }
        drawPath(
            path = pointerPath,
            color = Color.Red
        )

        drawCircle(
            color = Color.Gray,
            radius = 10f,
            center = Offset(center.x, center.y - radius - 15)
        )

    }
}
