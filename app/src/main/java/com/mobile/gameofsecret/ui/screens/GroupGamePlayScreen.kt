package com.mobile.gameofsecret.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.mobile.gameofsecret.DestinationScreen
import com.mobile.gameofsecret.R
import com.mobile.gameofsecret.data.model.DifficultyLevel
import com.mobile.gameofsecret.data.model.GameStatus
import com.mobile.gameofsecret.data.model.GroupQuestion
import com.mobile.gameofsecret.data.model.QuestionType
import com.mobile.gameofsecret.ui.components.SectorShape
import com.mobile.gameofsecret.ui.components.Triangle
import com.mobile.gameofsecret.ui.theme.*
import com.mobile.gameofsecret.viewmodels.GroupGameViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
fun GroupGamePlayScreen(
    navController: NavController,
    groupGameViewModel: GroupGameViewModel,
    gameCode: String
) {
    val waitingRoomState by groupGameViewModel.waitingRoomState.collectAsState()
    val game = waitingRoomState.game

    var showQuestion by remember { mutableStateOf(false) }
    var showFinishedDialog by remember { mutableStateOf(false) }
    var showExitDialog by remember { mutableStateOf(false) }
    var isSpinning by remember { mutableStateOf(false) }
    var selectedPlayerName by remember { mutableStateOf("") }
    var selectedPlayerIndex by remember { mutableStateOf(-1) }

    val rotation = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()

    // Oyun dinlemeyi başlat
    LaunchedEffect(gameCode) {
        groupGameViewModel.startListeningToGame(gameCode)
    }

    // Oyun bitti mi kontrol et
    LaunchedEffect(game?.status) {
        if (game?.status == GameStatus.FINISHED.name) {
            showFinishedDialog = true
        }
    }

    // Çıkış dialog
    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = { Text(stringResource(R.string.exit_game_title)) },
            text = { Text(stringResource(R.string.exit_game_warning)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showExitDialog = false
                        groupGameViewModel.leaveGame(gameCode) {
                            navController.navigate(DestinationScreen.Menu.route) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    }
                ) {
                    Text(stringResource(R.string.exit), color = Color(0xFFE53935))
                }
            },
            dismissButton = {
                TextButton(onClick = { showExitDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }

    // Oyun bitti dialog
    if (showFinishedDialog) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text(stringResource(R.string.game_finished_title)) },
            text = {
                Column {
                    Text(stringResource(R.string.game_finished_message))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.total_questions_played, game?.questions?.size ?: 0),
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showFinishedDialog = false
                        groupGameViewModel.leaveGame(gameCode) {
                            navController.navigate(DestinationScreen.Menu.route) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    },
                    colors = buttonColors1
                ) {
                    Text(stringResource(R.string.back_to_menu))
                }
            }
        )
    }

    if (game == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(background),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color.White)
        }
        return
    }

    val players = game.players
    val playerNames = players.map { it.name }
    val currentQuestion = game.questions.getOrNull(game.currentQuestionIndex)
    val questionProgress = "${game.currentQuestionIndex + 1}/${game.questions.size}"

    // Çark çevirme fonksiyonu
    val spinWheel: () -> Unit = {
        if (!isSpinning && playerNames.isNotEmpty()) {
            isSpinning = true
            showQuestion = false
            coroutineScope.launch {
                val totalRotation = 1080f + Random.nextFloat() * 360f
                rotation.snapTo(0f)
                rotation.animateTo(
                    targetValue = totalRotation,
                    animationSpec = tween(durationMillis = 3000, easing = FastOutSlowInEasing)
                )
                val sectorIndex = ((360f - (rotation.value % 360f)) / (360f / playerNames.size)).toInt()
                selectedPlayerName = playerNames.getOrElse(sectorIndex) { playerNames.first() }
                selectedPlayerIndex = sectorIndex

                // Firestore'da seçilen oyuncuyu güncelle
                groupGameViewModel.setSelectedPlayer(sectorIndex, gameCode)

                delay(500)
                showQuestion = true
                isSpinning = false
            }
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
            .padding(WindowInsets.systemBars.asPaddingValues()),
        topBar = {
            // Özel header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(color = background),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { showExitDialog = true }
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Exit",
                        tint = Color.White
                    )
                }

                Text(
                    text = stringResource(R.string.group_game),
                    fontWeight = FontWeight.W500,
                    fontSize = 22.sp,
                    color = textColor
                )

                // Soru sayacı
                Card(
                    modifier = Modifier.padding(end = 8.dp),
                    colors = cardcolor3,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = questionProgress,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(background)
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Çark veya Soru gösterimi
            AnimatedVisibility(
                visible = !showQuestion,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Çark gösterimi
                    Box(
                        modifier = Modifier
                            .offset(y = 15.dp)
                            .zIndex(1f)
                    ) {
                        Triangle(
                            color = Color.Red,
                            modifier = Modifier
                                .width(18.dp)
                                .height(27.dp)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(260.dp)
                            .rotate(rotation.value)
                            .border(6.dp, circleColor, CircleShape)
                    ) {
                        playerNames.forEachIndexed { index, name ->
                            val startAngle = index * (360f / playerNames.size)

                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .rotate(startAngle)
                                    .clip(SectorShape(360f / playerNames.size))
                                    .background(sectorColors[index % sectorColors.size])
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .rotate(startAngle + 360f / playerNames.size / 2),
                                contentAlignment = Alignment.TopCenter
                            ) {
                                Text(
                                    text = name,
                                    modifier = Modifier
                                        .padding(top = 35.dp)
                                        .rotate(-(startAngle + 360f / playerNames.size / 2)),
                                    color = Color.White,
                                    fontSize = if (playerNames.size > 6) 12.sp else 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .align(Alignment.Center)
                                .background(Color.White, CircleShape)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Çevir butonu
                    Button(
                        onClick = spinWheel,
                        enabled = !isSpinning,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = buttonColors1,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        if (isSpinning) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color.Black
                            )
                        } else {
                            Text(
                                text = stringResource(R.string.spin_the_wheel),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Soru gösterimi
            AnimatedVisibility(
                visible = showQuestion && currentQuestion != null,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut()
            ) {
                currentQuestion?.let { question ->
                    QuestionDisplay(
                        question = question,
                        playerName = selectedPlayerName,
                        showAuthor = game.showQuestionAuthor,
                        onNextQuestion = {
                            showQuestion = false
                            groupGameViewModel.moveToNextQuestion(gameCode, null)
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun QuestionDisplay(
    question: GroupQuestion,
    playerName: String,
    showAuthor: Boolean,
    onNextQuestion: () -> Unit
) {
    val questionType = try {
        QuestionType.valueOf(question.type)
    } catch (e: Exception) {
        QuestionType.TRUTH
    }

    val difficulty = try {
        DifficultyLevel.valueOf(question.difficulty)
    } catch (e: Exception) {
        DifficultyLevel.MEDIUM
    }

    val typeColor = if (questionType == QuestionType.TRUTH) Color(0xFF4CAF50) else Color(0xFFE53935)
    val typeName = if (questionType == QuestionType.TRUTH) stringResource(R.string.truth) else stringResource(R.string.dare)

    val difficultyColor = when (difficulty) {
        DifficultyLevel.EASY -> Color(0xFF4CAF50)
        DifficultyLevel.MEDIUM -> Color(0xFFFF9800)
        DifficultyLevel.HARD -> Color(0xFFE53935)
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Seçilen oyuncu
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardColors(
                containerColor = typeColor.copy(alpha = 0.2f),
                contentColor = Color.White,
                disabledContainerColor = typeColor.copy(alpha = 0.2f),
                disabledContentColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = playerName,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Soru tipi badge
                Row(
                    horizontalArrangement = Arrangement.Center
                ) {
                    Card(
                        colors = CardColors(
                            containerColor = typeColor,
                            contentColor = Color.White,
                            disabledContainerColor = typeColor,
                            disabledContentColor = Color.White
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = typeName,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Zorluk badge
                    Card(
                        colors = CardColors(
                            containerColor = difficultyColor.copy(alpha = 0.3f),
                            contentColor = difficultyColor,
                            disabledContainerColor = difficultyColor.copy(alpha = 0.3f),
                            disabledContentColor = difficultyColor
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = difficulty.name.lowercase().replaceFirstChar { it.uppercase() },
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Soru kartı
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = cardcolor,
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = question.question,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    lineHeight = 28.sp
                )

                if (showAuthor && question.addedByName.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "- ${question.addedByName}",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.6f),
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Sonraki soru butonu
        Button(
            onClick = onNextQuestion,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = buttonColors2,
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = stringResource(R.string.next_question),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
