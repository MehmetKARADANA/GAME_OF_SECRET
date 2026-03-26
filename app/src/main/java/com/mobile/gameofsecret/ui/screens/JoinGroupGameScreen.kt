package com.mobile.gameofsecret.ui.screens

import androidx.compose.foundation.background
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
fun JoinGroupGameScreen(
    navController: NavController,
    groupGameViewModel: GroupGameViewModel,
    gameCodeFromDeepLink: String? = null
) {
    var gameCode by remember { mutableStateOf(gameCodeFromDeepLink ?: "") }
    var playerName by remember { mutableStateOf("") }
    var isJoining by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val scrollState = rememberScrollState()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
            .padding(WindowInsets.systemBars.asPaddingValues()),
        topBar = {
            PreHeader(navController, stringResource(R.string.join_game))
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
            Spacer(modifier = Modifier.height(32.dp))

            // Bilgi kartı
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = cardcolor3,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = stringResource(R.string.join_game_info),
                    modifier = Modifier.padding(16.dp),
                    color = Color.White,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Oyun kodu girişi
            Text(
                text = stringResource(R.string.game_code),
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = gameCode,
                onValueChange = {
                    // Sadece alfanumerik karakterler ve max 6 karakter
                    if (it.length <= 6 && it.all { char -> char.isLetterOrDigit() }) {
                        gameCode = it.uppercase()
                        errorMessage = null
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("ABC123", color = Color.Gray) },
                colors = textFieldColor(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Characters,
                    imeAction = ImeAction.Next
                ),
                textStyle = LocalTextStyle.current.copy(
                    textAlign = TextAlign.Center,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 4.sp
                ),
                enabled = gameCodeFromDeepLink == null // Deep link'ten geldiyse düzenlemeyi kapat
            )

            if (gameCodeFromDeepLink != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.game_code_from_link),
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 12.sp
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
                value = playerName,
                onValueChange = {
                    if (it.length <= 10) {
                        playerName = it
                        errorMessage = null
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(stringResource(R.string.enter_your_name), color = Color.Gray) },
                leadingIcon = {
                    Icon(Icons.Default.Person, contentDescription = null, tint = Color.White)
                },
                supportingText = {
                    Text(
                        text = "${playerName.length}/10",
                        color = Color.White.copy(alpha = 0.6f)
                    )
                },
                colors = textFieldColor(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Done
                )
            )

            // Hata mesajı
            errorMessage?.let { error ->
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

            // Katıl butonu
            Button(
                onClick = {
                    isJoining = true
                    errorMessage = null

                    groupGameViewModel.joinGame(
                        gameCode = gameCode,
                        playerName = playerName,
                        onSuccess = {
                            isJoining = false
                            // Zaten sorularını eklediyse bekleme odasına git
                            navController.navigate(DestinationScreen.AddQuestions.createRoute(gameCode)) {
                                popUpTo(DestinationScreen.JoinGroupGame.route) { inclusive = true }
                            }
                        },
                        onError = { error ->
                            isJoining = false
                            errorMessage = error
                        }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = buttonColors1,
                shape = RoundedCornerShape(12.dp),
                enabled = gameCode.length == 6 && playerName.trim().length >= 2 && !isJoining
            ) {
                if (isJoining) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.Black
                    )
                } else {
                    Text(
                        text = stringResource(R.string.join_game_button),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Alt bilgi
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = cardcolor,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = stringResource(R.string.join_game_note),
                    modifier = Modifier.padding(12.dp),
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
