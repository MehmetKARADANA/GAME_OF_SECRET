package com.mobile.gameofsecret.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mobile.gameofsecret.DestinationScreen
import com.mobile.gameofsecret.R
import com.mobile.gameofsecret.data.model.GameStatus
import com.mobile.gameofsecret.data.model.GroupGame
import com.mobile.gameofsecret.data.model.GroupPlayer
import com.mobile.gameofsecret.ui.components.PreHeader
import com.mobile.gameofsecret.ui.components.QRCodeView
import com.mobile.gameofsecret.ui.theme.*
import com.mobile.gameofsecret.viewmodels.GroupGameViewModel

@Composable
fun WaitingRoomScreen(
    navController: NavController,
    groupGameViewModel: GroupGameViewModel,
    gameCode: String
) {
    val waitingRoomState by groupGameViewModel.waitingRoomState.collectAsState()
    val context = LocalContext.current
    var showQRCode by remember { mutableStateOf(false) }
    var showLeaveDialog by remember { mutableStateOf(false) }
    var showGameFinishedDialog by remember { mutableStateOf(false) }
    var startError by remember { mutableStateOf<String?>(null) }
    var hasNavigatedToGame by remember { mutableStateOf(false) }

    // Oyun dinlemeyi başlat
    LaunchedEffect(gameCode) {
        groupGameViewModel.startListeningToGame(gameCode)
    }

    // Oyun başladığında yönlendir - SADECE HOST (sadece bir kez)
    LaunchedEffect(waitingRoomState.game?.status) {
        if (waitingRoomState.game?.status == GameStatus.PLAYING.name && !hasNavigatedToGame) {
            if (groupGameViewModel.isCurrentDeviceHost()) {
                hasNavigatedToGame = true
                // Sadece host oyun ekranına gider
                navController.navigate(DestinationScreen.GroupGamePlay.createRoute(gameCode)) {
                    popUpTo(DestinationScreen.WaitingRoom.route) { inclusive = true }
                }
            }
            // Diğer oyuncular waiting room'da kalır ve "Oyun devam ediyor" mesajı görür
        }
        // Oyun bittiğinde misafir oyuncuları bilgilendir
        if (waitingRoomState.game?.status == GameStatus.FINISHED.name) {
            showGameFinishedDialog = true
        }
    }

    // Oyundan ayrılma dialog
    if (showLeaveDialog) {
        AlertDialog(
            onDismissRequest = { showLeaveDialog = false },
            title = { Text(stringResource(R.string.leave_game_title)) },
            text = {
                Text(
                    if (groupGameViewModel.isCurrentDeviceHost()) {
                        stringResource(R.string.leave_game_host_warning)
                    } else {
                        stringResource(R.string.leave_game_warning)
                    }
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLeaveDialog = false
                        groupGameViewModel.leaveGame(gameCode) {
                            navController.navigate(DestinationScreen.Pre.route) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    }
                ) {
                    Text(stringResource(R.string.leave), color = Color(0xFFE53935))
                }
            },
            dismissButton = {
                TextButton(onClick = { showLeaveDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }

    // QR Kod dialog
    if (showQRCode) {
        AlertDialog(
            onDismissRequest = { showQRCode = false },
            containerColor = background,
            titleContentColor = Color.White,
            textContentColor = Color.White,
            title = { Text(stringResource(R.string.scan_to_join), textAlign = TextAlign.Center) },
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    QRCodeView(
                        content = "https://gameofsecret.app/g/$gameCode",
                        size = 200
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = gameCode,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 4.sp,
                        color = Color.White
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showQRCode = false }) {
                    Text(stringResource(R.string.close), color = Color.White)
                }
            }
        )
    }

    // Oyun bitti dialog (misafir oyuncular için)
    if (showGameFinishedDialog) {
        AlertDialog(
            onDismissRequest = { },
            containerColor = background,
            titleContentColor = Color.White,
            textContentColor = Color.White,
            title = {
                Text(
                    text = stringResource(R.string.game_finished_title),
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
            },
            text = {
                Text(
                    text = stringResource(R.string.game_finished_message),
                    color = Color.White
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showGameFinishedDialog = false
                        groupGameViewModel.leaveGame(gameCode) {
                            navController.navigate(DestinationScreen.Pre.route) {
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

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
            .padding(WindowInsets.systemBars.asPaddingValues()),
        topBar = {
            PreHeader(navController, stringResource(R.string.waiting_room))
        }
    ) { paddingValues ->
        if (waitingRoomState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(background),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.White)
            }
        } else if (waitingRoomState.error != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(background)
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = waitingRoomState.error!!,
                        color = Color.White,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            navController.navigate(DestinationScreen.Menu.route) {
                                popUpTo(0) { inclusive = true }
                            }
                        },
                        colors = buttonColors1
                    ) {
                        Text(stringResource(R.string.back_to_menu))
                    }
                }
            }
        } else {
            val game = waitingRoomState.game
            if (game != null) {
                // Oyun başladıysa ve host değilsek, "Oyun devam ediyor" ekranı göster
                if (game.status == GameStatus.PLAYING.name && !groupGameViewModel.isCurrentDeviceHost()) {
                    GameInProgressScreen(
                        game = game,
                        onLeave = {
                            groupGameViewModel.leaveGame(gameCode) {
                                navController.navigate(DestinationScreen.Pre.route) {
                                    popUpTo(0) { inclusive = true }
                                }
                            }
                        }
                    )
                } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(background)
                        .padding(paddingValues)
                        .padding(horizontal = 16.dp)
                ) {
                    Spacer(modifier = Modifier.height(16.dp))

                    // Oyun kodu kartı
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = cardcolor3,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = stringResource(R.string.game_code),
                                fontSize = 14.sp,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = game.gameCode,
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                letterSpacing = 6.sp
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            // Paylaşım butonları
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                // Kopyala
                                Button(
                                    onClick = {
                                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                        val clip = ClipData.newPlainText("Game Code", game.gameCode)
                                        clipboard.setPrimaryClip(clip)
                                        Toast.makeText(context, context.getString(R.string.code_copied), Toast.LENGTH_SHORT).show()
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color.White.copy(alpha = 0.15f),
                                        contentColor = Color.White
                                    ),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(stringResource(R.string.copy))
                                }

                                // Paylaş
                                Button(
                                    onClick = {
                                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                            type = "text/plain"
                                            putExtra(Intent.EXTRA_TEXT,
                                                context.getString(R.string.share_game_message, game.gameCode, game.getShareableLink())
                                            )
                                        }
                                        context.startActivity(Intent.createChooser(shareIntent, context.getString(R.string.share_via)))
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color.White.copy(alpha = 0.15f),
                                        contentColor = Color.White
                                    ),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(stringResource(R.string.share))
                                }

                                // QR Kod
                                Button(
                                    onClick = { showQRCode = true },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color.White.copy(alpha = 0.15f),
                                        contentColor = Color.White
                                    ),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text("QR")
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Oyuncu listesi başlığı
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.players),
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "${game.players.size}/${game.maxPlayers}",
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Oyuncu listesi
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(game.players) { player ->
                            PlayerListItem(
                                player = player,
                                questionsPerPlayer = game.questionsPerPlayer,
                                isCurrentDevice = player.deviceId == groupGameViewModel.deviceId
                            )
                        }
                    }

                    // Durum bilgisi
                    Spacer(modifier = Modifier.height(16.dp))

                    val readyCount = game.getReadyPlayersCount()
                    val totalPlayers = game.players.size

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = cardcolor,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(R.string.ready_status, readyCount, totalPlayers),
                                color = Color.White,
                                fontSize = 14.sp
                            )
                            if (game.areAllPlayersReady()) {
                                Icon(
                                    Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color(0xFF4CAF50)
                                )
                            }
                        }
                    }

                    // Hata mesajı
                    startError?.let { error ->
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = error,
                            color = Color(0xFFE53935),
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Butonlar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Ayrıl butonu
                        Button(
                            onClick = { showLeaveDialog = true },
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFE53935).copy(alpha = 0.2f),
                                contentColor = Color(0xFFE53935)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(stringResource(R.string.leave))
                        }

                        // Host için "Oyunu Başlat" butonu
                        if (groupGameViewModel.isCurrentDeviceHost()) {
                            Button(
                                onClick = {
                                    startError = null
                                    groupGameViewModel.startGame(
                                        gameCode = gameCode,
                                        onSuccess = {
                                            // Navigation LaunchedEffect'te yapılıyor
                                        },
                                        onError = { error ->
                                            startError = error
                                        }
                                    )
                                },
                                modifier = Modifier
                                    .weight(2f)
                                    .height(56.dp),
                                colors = buttonColors1,
                                shape = RoundedCornerShape(12.dp),
                                enabled = game.canStartGame()
                            ) {
                                Text(
                                    text = stringResource(R.string.start_game),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        } else {
                            // Normal oyuncular için "Bekleniyor" göstergesi
                            Card(
                                modifier = Modifier
                                    .weight(2f)
                                    .height(56.dp),
                                colors = cardcolor,
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(20.dp),
                                            color = Color.White,
                                            strokeWidth = 2.dp
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Text(
                                            text = stringResource(R.string.waiting_for_host),
                                            color = Color.White,
                                            fontSize = 14.sp
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
                } // else bloğu kapanışı (normal waiting room)
            }
        }
    }
}

@Composable
fun GameInProgressScreen(
    game: GroupGame,
    onLeave: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Animasyonlu ikon
        CircularProgressIndicator(
            modifier = Modifier.size(80.dp),
            color = Color(0xFF4CAF50),
            strokeWidth = 6.dp
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = stringResource(R.string.game_in_progress),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.host_managing_game),
            fontSize = 16.sp,
            color = Color.White.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Kalan soru sayısı
        val remainingQuestions = game.questions.count { !it.isUsed }
        Text(
            text = stringResource(R.string.remaining_questions, remainingQuestions),
            fontSize = 14.sp,
            color = Color.White.copy(alpha = 0.5f),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Ayrıl butonu
        Button(
            onClick = onLeave,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFE53935).copy(alpha = 0.2f),
                contentColor = Color(0xFFE53935)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(stringResource(R.string.leave_game_title))
        }
    }
}

@Composable
fun PlayerListItem(
    player: GroupPlayer,
    questionsPerPlayer: Int,
    isCurrentDevice: Boolean
) {
    val isReady = player.isReady

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = if (isReady) {
            CardColors(
                containerColor = Color(0xFF4CAF50).copy(alpha = 0.2f),
                contentColor = Color.White,
                disabledContainerColor = background,
                disabledContentColor = Color.Gray
            )
        } else {
            cardcolor
        },
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(
                        if (player.isHost) Color(0xFFFFB300) else Color(0xFF2196F3)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = player.name.take(1).uppercase(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // İsim ve durum
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = player.name,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                    if (player.isHost) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Card(
                            colors = CardColors(
                                containerColor = Color(0xFFFFB300),
                                contentColor = Color.Black,
                                disabledContainerColor = Color(0xFFFFB300),
                                disabledContentColor = Color.Black
                            ),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "HOST",
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    if (isCurrentDevice) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "(Sen)",
                            color = Color.Cyan,
                            fontSize = 12.sp
                        )
                    }
                }

                // Soru durumu
                Text(
                    text = "${player.questionsAdded}/$questionsPerPlayer ${if (isReady) "✓" else "..."}",
                    color = if (isReady) Color(0xFF4CAF50) else Color.White.copy(alpha = 0.6f),
                    fontSize = 12.sp
                )
            }

            // Hazır/Bekliyor göstergesi
            if (isReady) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = "Ready",
                    tint = Color(0xFF4CAF50),
                    modifier = Modifier.size(24.dp)
                )
            } else {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = Color.White.copy(alpha = 0.5f),
                    strokeWidth = 2.dp
                )
            }
        }
    }
}
