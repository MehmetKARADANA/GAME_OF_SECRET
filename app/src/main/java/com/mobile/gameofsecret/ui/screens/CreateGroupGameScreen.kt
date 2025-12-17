package com.mobile.gameofsecret.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mobile.gameofsecret.DestinationScreen
import com.mobile.gameofsecret.R
import com.mobile.gameofsecret.ui.components.PreHeader
import com.mobile.gameofsecret.ui.theme.*
import com.mobile.gameofsecret.viewmodels.GroupGameViewModel

@Composable
fun CreateGroupGameScreen(
    navController: NavController,
    groupGameViewModel: GroupGameViewModel
) {
    val createGameState by groupGameViewModel.createGameState.collectAsState()
    val scrollState = rememberScrollState()

    // Oyuncu sayısı seçenekleri
    val playerCountOptions = listOf(2, 3, 4, 5, 6, 7, 8, 9, 10)

    // Soru sayısı seçenekleri
    val questionCountOptions = listOf(3, 5, 7, 10)

    LaunchedEffect(createGameState.createdGameCode) {
        createGameState.createdGameCode?.let { gameCode ->
            navController.navigate(DestinationScreen.AddQuestions.createRoute(gameCode)) {
                popUpTo(DestinationScreen.CreateGroupGame.route) { inclusive = true }
            }
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
            .padding(WindowInsets.systemBars.asPaddingValues()),
        topBar = {
            PreHeader(navController, stringResource(R.string.create_group_game))
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(background)
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Bilgi kartı
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = cardcolor3,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = stringResource(R.string.create_game_info),
                    modifier = Modifier.padding(16.dp),
                    color = Color.White,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // İsim girişi
            Text(
                text = stringResource(R.string.your_name),
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = createGameState.hostName,
                onValueChange = { groupGameViewModel.updateHostName(it) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(stringResource(R.string.enter_your_name), color = Color.Gray) },
                leadingIcon = {
                    Icon(Icons.Default.Person, contentDescription = null, tint = Color.White)
                },
                colors = textFieldColor(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Done
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Oyuncu sayısı seçimi
            Text(
                text = stringResource(R.string.player_count),
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Oyuncu sayısı grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                playerCountOptions.chunked(5).forEach { row ->
                    row.forEach { count ->
                        SelectableChip(
                            text = count.toString(),
                            isSelected = createGameState.maxPlayers == count,
                            onClick = { groupGameViewModel.updateMaxPlayers(count) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // İkinci satır
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                playerCountOptions.drop(5).forEach { count ->
                    SelectableChip(
                        text = count.toString(),
                        isSelected = createGameState.maxPlayers == count,
                        onClick = { groupGameViewModel.updateMaxPlayers(count) },
                        modifier = Modifier.weight(1f)
                    )
                }
                // Boşluk doldurmak için
                repeat(5 - playerCountOptions.drop(5).size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Soru sayısı seçimi
            Text(
                text = stringResource(R.string.questions_per_player),
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                questionCountOptions.forEach { count ->
                    SelectableChip(
                        text = "$count",
                        isSelected = createGameState.questionsPerPlayer == count,
                        onClick = { groupGameViewModel.updateQuestionsPerPlayer(count) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Toplam soru bilgisi
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = cardcolor,
                shape = RoundedCornerShape(8.dp)
            ) {
                val totalQuestions = createGameState.maxPlayers * createGameState.questionsPerPlayer
                Text(
                    text = stringResource(R.string.total_questions_info, totalQuestions),
                    modifier = Modifier.padding(12.dp),
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center
                )
            }

            // Hata mesajı
            createGameState.error?.let { error ->
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardColors(
                        containerColor = Color(0xFFD32F2F),
                        contentColor = Color.White,
                        disabledContainerColor = Color(0xFFD32F2F),
                        disabledContentColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = error,
                        modifier = Modifier.padding(12.dp),
                        color = Color.White,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Oluştur butonu
            Button(
                onClick = {
                    groupGameViewModel.createGame { gameCode ->
                        // Navigation LaunchedEffect'te yapılıyor
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = buttonColors1,
                shape = RoundedCornerShape(12.dp),
                enabled = !createGameState.isCreating
            ) {
                if (createGameState.isCreating) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.Black
                    )
                } else {
                    Text(
                        text = stringResource(R.string.create_game),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun SelectableChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(48.dp)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) Color.Cyan else Color.White.copy(alpha = 0.3f),
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { onClick() },
        colors = if (isSelected) cardcolor3 else cardcolor,
        shape = RoundedCornerShape(8.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}
