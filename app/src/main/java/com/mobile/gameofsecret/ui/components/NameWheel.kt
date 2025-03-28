package com.mobile.gameofsecret.ui.components


import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mobile.gameofsecret.ui.theme.background
import kotlinx.coroutines.launch
import kotlin.random.Random
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.zIndex
import com.mobile.gameofsecret.data.model.Gamer
import com.mobile.gameofsecret.ui.theme.sectorColors


/*
@Composable
fun NameWheel(gamerList : List<Gamer>) {
    val names = gamerList.map { it.name }

    // Renkler

    // State tanımlamaları
    var isSpinning by remember { mutableStateOf(false) }
    val rotation = remember { Animatable(0f) }
    var selectedName by remember { mutableStateOf("") }

    // Coroutine Scope
    val coroutineScope = rememberCoroutineScope()

    // Çarkı döndürme fonksiyonu
    val spinWheel: () -> Unit = {
        if (!isSpinning) {
            isSpinning = true

            coroutineScope.launch {
                // Sabit dönüş açısı (1440° sabit dönüş ve rastgele eklenen 360°)
                val totalRotation = 1080f + Random.nextFloat() * 360f

                // Rotasyonu sıfırlıyoruz, her dönüş için
                rotation.snapTo(0f)

                // Animasyonu başlatıyoruz
                rotation.animateTo(
                    targetValue = totalRotation,
                    animationSpec = tween(
                        durationMillis = 3000,  // Dönüş süresi
                        easing = FastOutSlowInEasing
                    )
                )

                // Çarkın hangi bölümüne denk geldiğini hesapla
                val sectorIndex = ((360f - (rotation.value % 360f)) / (360f / names.size)).toInt()

                selectedName = names[sectorIndex]
                isSpinning = false
            }
        }
    }

    Column(
        modifier = Modifier
            .background(background)
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Sabit Üçgen İşaretçi (12 saat hizasında)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
            // contentAlignment = Alignment.TopCenter
        ) {
            Triangle(
                color = Color.Red,
                modifier = Modifier
                    .width(20.dp)
                    .height(30.dp)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            // Çarkın görsel temsili
            Box(
                modifier = Modifier
                    .clickable {
                        spinWheel.invoke()
                    }
                    .size(280.dp)
                    .rotate(rotation.value)
            ) {
                // Bölümlerin çizimi
                names.forEachIndexed { index, name ->
                    val startAngle = index * (360f / names.size)

                    // Her bir dilim için yay
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .rotate(startAngle)
                            .clip(SectorShape(360f / names.size))
                            .background(sectorColors[index % sectorColors.size]) // Rengi burada ekliyoruz
                    )

                    // İsim yazıları
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .rotate(startAngle + 360f / names.size / 2)
                    ) {
                        Text(
                            text = name,
                            modifier = Modifier
                                .offset(x = 100.dp)
                                .rotate(-(startAngle + 360f / names.size / 2)),
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Çarkı Döndür Butonu
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            verticalAlignment = Alignment.CenterVertically
            , horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = spinWheel,
                enabled = !isSpinning
            ) {
                Text("Çarkı Döndür", fontSize = 18.sp)
            }
        }

        // Seçilen ismi göster
        if (selectedName.isNotEmpty()) {
            Text(
                text = "Kazanan: $selectedName 🎉",
                modifier = Modifier,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Green
            )
        }
    }
}
*/
@Composable
fun NameWheel(gamerList: List<Gamer>) {
    val names = gamerList.map { it.name }

    var isSpinning by remember { mutableStateOf(false) }
    val rotation = remember { Animatable(0f) }
    var selectedName by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    val spinWheel: () -> Unit = {
        if (!isSpinning) {
            isSpinning = true
            coroutineScope.launch {
                val totalRotation = 1080f + Random.nextFloat() * 360f
                rotation.snapTo(0f)
                rotation.animateTo(
                    targetValue = totalRotation,
                    animationSpec = tween(durationMillis = 3000, easing = FastOutSlowInEasing)
                )
                val sectorIndex = ((360f - (rotation.value % 360f)) / (360f / names.size)).toInt()
                selectedName = names[sectorIndex]
                isSpinning = false
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .offset(y = 15.dp) // Pin biraz çarkın üzerine gelsin
                .zIndex(1f) // Üstte dursun
        ) {
            Triangle(
                color = Color.Red,
                modifier = Modifier
                    .width(24.dp)
                    .height(36.dp)
            )
        }

        // **Çark**
        Box(
            modifier = Modifier
                .size(280.dp)
                .rotate(rotation.value)
                .border(4.dp, Color.Black, CircleShape)
                .clickable { spinWheel() }
        ) {
            // Bölümlerin çizimi
            names.forEachIndexed { index, name ->
                val startAngle = index * (360f / names.size)

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .rotate(startAngle)
                        .clip(SectorShape(360f / names.size))
                        .background(sectorColors[index % sectorColors.size])
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .rotate(startAngle + 360f / names.size / 2)
                ) {
                    Text(
                        text = name,
                        modifier = Modifier
                            .offset(x = 100.dp)
                            .rotate(-(startAngle + 360f / names.size / 2)),
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .align(Alignment.Center)
                    .background(Color.White, CircleShape)
            )
        }

        Button(
            onClick = spinWheel,
            enabled = !isSpinning,
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Çarkı Döndür", fontSize = 18.sp)
        }

        if (selectedName.isNotEmpty()) {
            Text(
                text = "Kazanan: $selectedName 🎉",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Green
            )
        }
    }
}

// Özel Sector Şekli
class SectorShape(private val sweepAngle: Float) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path().apply {
            moveTo(size.width / 2f, size.height / 2f)
            lineTo(size.width / 2f, 0f)
            arcTo(
                rect = Rect(
                    left = 0f,
                    top = 0f,
                    right = size.width,
                    bottom = size.height
                ),
                startAngleDegrees = 270f,
                sweepAngleDegrees = sweepAngle,
                forceMoveTo = false
            )
            close()
        }
        return Outline.Generic(path)
    }
}

// Üçgen Şekli
@Composable
fun Triangle(
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .rotate(180f)
            .clip(TriangleShape())
            .background(color)
    )
}

class TriangleShape : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path().apply {
            moveTo(size.width / 2f, 0f)
            lineTo(size.width, size.height)
            lineTo(0f, size.height)
            close()
        }
        return Outline.Generic(path)
    }
}
