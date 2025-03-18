package com.mobile.gameofsecret.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberBasicTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import kotlin.random.Random


@Composable
fun Passionflower() {
    val colors = listOf(
        Color.Red, Color.Blue, Color.Green, Color.Yellow,
        Color.Magenta, Color.Cyan, Color.Gray, Color(0xFFFFA500)
    )
    var rotation by remember { mutableStateOf(0f) }
    val animatedRotation by animateFloatAsState(
        targetValue = rotation,
        animationSpec = tween(durationMillis = 2000, easing = FastOutSlowInEasing),
        label = "Wheel Rotation"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White).pointerInput(Unit){
                detectTapGestures {
                    rotation+= Random.nextInt(180,720).toFloat()
                }
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Canvas(modifier = Modifier.size(300.dp)) {
            val radius = size.minDimension / 2f
            val center = Offset(radius, radius)
            val sliceAngle = 360f / colors.size

            for (i in colors.indices) {
                rotate(animatedRotation + (i * sliceAngle), center) {
                    drawArc(
                        color = colors[i],
                        startAngle = 0f,
                        sweepAngle = sliceAngle,
                        useCenter = true,
                    )
                }
                drawCircle(color = Color.Black, radius = 10f, center = center)
            }

            /* drawCircle(brush = Brush.radialGradient(listOf(Color.White,Color.Red,Color.Blue,Color.Green)),
                 radius =80f,
                 center = Offset(size.width/2,size.height/2)
             )*/
        }
    }


}