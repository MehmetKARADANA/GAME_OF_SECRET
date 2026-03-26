package com.mobile.gameofsecret.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
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
import com.mobile.gameofsecret.data.model.DifficultyLevel
import com.mobile.gameofsecret.data.model.QuestionType
import com.mobile.gameofsecret.ui.components.PreHeader
import com.mobile.gameofsecret.ui.theme.*
import com.mobile.gameofsecret.viewmodels.GroupGameViewModel

@Composable
fun AddQuestionsScreen(
    navController: NavController,
    groupGameViewModel: GroupGameViewModel,
    gameCode: String
) {
    val addQuestionsState by groupGameViewModel.addQuestionsState.collectAsState()
    val waitingRoomState by groupGameViewModel.waitingRoomState.collectAsState()

    // Oyun bilgisini dinlemeye başla
    LaunchedEffect(gameCode) {
        groupGameViewModel.startListeningToGame(gameCode)
        waitingRoomState.game?.questionsPerPlayer?.let {
            groupGameViewModel.setRequiredQuestions(it)
        }
    }

    // Gerekli soru sayısını güncelle
    LaunchedEffect(waitingRoomState.game) {
        waitingRoomState.game?.questionsPerPlayer?.let {
            groupGameViewModel.setRequiredQuestions(it)
        }
    }

    val requiredQuestions = waitingRoomState.game?.questionsPerPlayer ?: addQuestionsState.requiredQuestions
    val addedCount = addQuestionsState.addedQuestions.size
    val isComplete = addedCount >= requiredQuestions

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
            .padding(WindowInsets.systemBars.asPaddingValues()),
        topBar = {
            PreHeader(navController, stringResource(R.string.add_questions))
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(background)
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            // Progress göstergesi
            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = cardcolor4,
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "$addedCount / $requiredQuestions",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = stringResource(R.string.questions_added),
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.8f)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    LinearProgressIndicator(
                        progress = { addedCount.toFloat() / requiredQuestions },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp),
                        color = if (isComplete) Color(0xFF4CAF50) else Color.White,
                        trackColor = Color.White.copy(alpha = 0.3f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Soru tipi seçimi
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                QuestionTypeChip(
                    text = stringResource(R.string.truth),
                    isSelected = addQuestionsState.currentType == QuestionType.TRUTH,
                    onClick = { groupGameViewModel.updateCurrentType(QuestionType.TRUTH) },
                    color = Color(0xFF4CAF50),
                    modifier = Modifier.weight(1f)
                )
                QuestionTypeChip(
                    text = stringResource(R.string.dare),
                    isSelected = addQuestionsState.currentType == QuestionType.DARE,
                    onClick = { groupGameViewModel.updateCurrentType(QuestionType.DARE) },
                    color = Color(0xFFE53935),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Zorluk seviyesi seçimi
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DifficultyChip(
                    text = stringResource(R.string.easy),
                    isSelected = addQuestionsState.currentDifficulty == DifficultyLevel.EASY,
                    onClick = { groupGameViewModel.updateCurrentDifficulty(DifficultyLevel.EASY) },
                    modifier = Modifier.weight(1f)
                )
                DifficultyChip(
                    text = stringResource(R.string.medium),
                    isSelected = addQuestionsState.currentDifficulty == DifficultyLevel.MEDIUM,
                    onClick = { groupGameViewModel.updateCurrentDifficulty(DifficultyLevel.MEDIUM) },
                    modifier = Modifier.weight(1f)
                )
                DifficultyChip(
                    text = stringResource(R.string.hard),
                    isSelected = addQuestionsState.currentDifficulty == DifficultyLevel.HARD,
                    onClick = { groupGameViewModel.updateCurrentDifficulty(DifficultyLevel.HARD) },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Soru girişi
            OutlinedTextField(
                value = addQuestionsState.currentQuestion,
                onValueChange = { groupGameViewModel.updateCurrentQuestion(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                placeholder = {
                    Text(
                        stringResource(R.string.enter_question_placeholder),
                        color = Color.Gray
                    )
                },
                colors = textFieldColor(),
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Done
                ),
                maxLines = 4
            )

            // Ekle butonu
            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { groupGameViewModel.addQuestion() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = buttonColors2,
                shape = RoundedCornerShape(8.dp),
                enabled = addQuestionsState.currentQuestion.trim().length >= 5 && addedCount < requiredQuestions
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(R.string.add_question))
            }

            // Hata mesajı
            addQuestionsState.error?.let { error ->
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = error,
                    color = Color(0xFFE53935),
                    fontSize = 14.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Eklenen sorular listesi
            Text(
                text = stringResource(R.string.your_questions),
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(addQuestionsState.addedQuestions) { question ->
                    QuestionItem(
                        question = question.question,
                        type = question.type,
                        difficulty = question.difficulty,
                        onDelete = { groupGameViewModel.removeQuestion(question.id) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tamamla butonu
            Button(
                onClick = {
                    groupGameViewModel.saveQuestionsAndMarkReady(gameCode) {
                        navController.navigate(DestinationScreen.WaitingRoom.createRoute(gameCode)) {
                            popUpTo(DestinationScreen.AddQuestions.route) { inclusive = true }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = buttonColors1,
                shape = RoundedCornerShape(12.dp),
                enabled = isComplete && !addQuestionsState.isSaving
            ) {
                if (addQuestionsState.isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.Black
                    )
                } else {
                    Text(
                        text = stringResource(R.string.complete_and_continue),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun QuestionTypeChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(48.dp)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) color else Color.White.copy(alpha = 0.3f),
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { onClick() },
        colors = CardColors(
            containerColor = if (isSelected) color else background,
            contentColor = Color.White,
            disabledContainerColor = background,
            disabledContentColor = Color.Gray
        ),
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

@Composable
fun DifficultyChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val difficultyColor = when (text) {
        "Easy", "Kolay" -> Color(0xFF4CAF50)
        "Medium", "Orta" -> Color(0xFFFF9800)
        "Hard", "Zor" -> Color(0xFFE53935)
        else -> Color.White
    }

    Card(
        modifier = modifier
            .height(40.dp)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) difficultyColor else Color.White.copy(alpha = 0.3f),
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { onClick() },
        colors = CardColors(
            containerColor = if (isSelected) difficultyColor.copy(alpha = 0.3f) else background,
            contentColor = Color.White,
            disabledContainerColor = background,
            disabledContentColor = Color.Gray
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = if (isSelected) difficultyColor else Color.White,
                fontSize = 14.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}

@Composable
fun QuestionItem(
    question: String,
    type: String,
    difficulty: String,
    onDelete: () -> Unit
) {
    val typeColor = if (type == QuestionType.TRUTH.name) Color(0xFF4CAF50) else Color(0xFFE53935)
    val difficultyColor = when (difficulty) {
        DifficultyLevel.EASY.name -> Color(0xFF4CAF50)
        DifficultyLevel.MEDIUM.name -> Color(0xFFFF9800)
        DifficultyLevel.HARD.name -> Color(0xFFE53935)
        else -> Color.White
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = cardcolor,
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Tip göstergesi
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(typeColor, CircleShape)
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Soru metni
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = question,
                    color = Color.White,
                    fontSize = 14.sp,
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row {
                    Text(
                        text = if (type == QuestionType.TRUTH.name) "Truth" else "Dare",
                        color = typeColor,
                        fontSize = 12.sp
                    )
                    Text(
                        text = " • ",
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 12.sp
                    )
                    Text(
                        text = difficulty.lowercase().replaceFirstChar { it.uppercase() },
                        color = difficultyColor,
                        fontSize = 12.sp
                    )
                }
            }

            // Sil butonu
            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "Delete",
                    tint = Color.White.copy(alpha = 0.7f)
                )
            }
        }
    }
}
