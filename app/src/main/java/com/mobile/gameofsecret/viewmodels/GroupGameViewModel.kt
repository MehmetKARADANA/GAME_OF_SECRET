package com.mobile.gameofsecret.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.firestore
import com.mobile.gameofsecret.data.GROUP_GAMES
import com.mobile.gameofsecret.data.model.*
import com.mobile.gameofsecret.utils.DeviceIdHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class GroupGameViewModel(application: Application) : BaseViewModel(application) {

    private val db = Firebase.firestore
    private val TAG = "GroupGameViewModel"

    // Device ID - uygulama başlatıldığında lazım olacak
    private var _deviceId: String = ""
    val deviceId: String get() = _deviceId

    // Oyun oluşturma state
    private val _createGameState = MutableStateFlow(CreateGameUiState())
    val createGameState: StateFlow<CreateGameUiState> = _createGameState.asStateFlow()

    // Bekleme odası state
    private val _waitingRoomState = MutableStateFlow(WaitingRoomUiState())
    val waitingRoomState: StateFlow<WaitingRoomUiState> = _waitingRoomState.asStateFlow()

    // Soru ekleme state
    private val _addQuestionsState = MutableStateFlow(AddQuestionsUiState())
    val addQuestionsState: StateFlow<AddQuestionsUiState> = _addQuestionsState.asStateFlow()

    // Oyun oynama state
    private val _gamePlayState = MutableStateFlow(GroupGamePlayUiState())
    val gamePlayState: StateFlow<GroupGamePlayUiState> = _gamePlayState.asStateFlow()

    // Mevcut oyun kodu
    private val _currentGameCode = MutableStateFlow<String?>(null)
    val currentGameCode: StateFlow<String?> = _currentGameCode.asStateFlow()

    // Firestore realtime listener
    private var gameListener: ListenerRegistration? = null

    init {
        _deviceId = DeviceIdHelper.getDeviceId(getApplication())
        Log.d(TAG, "Device ID: $_deviceId")
    }

    // ============== OYUN OLUŞTURMA ==============

    fun updateHostName(name: String) {
        _createGameState.value = _createGameState.value.copy(hostName = name)
    }

    fun updateMaxPlayers(count: Int) {
        _createGameState.value = _createGameState.value.copy(maxPlayers = count)
    }

    fun updateQuestionsPerPlayer(count: Int) {
        _createGameState.value = _createGameState.value.copy(questionsPerPlayer = count)
    }

    fun createGame(onSuccess: (String) -> Unit) {
        val state = _createGameState.value

        if (!DeviceIdHelper.isValidPlayerName(state.hostName)) {
            _createGameState.value = state.copy(error = "Lütfen geçerli bir isim girin (2-20 karakter)")
            return
        }

        viewModelScope.launch {
            _createGameState.value = state.copy(isCreating = true, error = null)

            try {
                // Benzersiz oyun kodu oluştur
                var gameCode: String
                var isUnique = false

                do {
                    gameCode = DeviceIdHelper.generateGameCode()
                    val existing = db.collection(GROUP_GAMES)
                        .whereEqualTo("gameCode", gameCode)
                        .get()
                        .await()
                    isUnique = existing.isEmpty
                } while (!isUnique)

                // Host oyuncu oluştur
                val hostPlayer = GroupPlayer(
                    deviceId = _deviceId,
                    name = state.hostName.trim(),
                    questionsAdded = 0,
                    isHost = true,
                    isReady = false
                )

                // Oyun oluştur
                val game = GroupGame(
                    gameId = gameCode,
                    gameCode = gameCode,
                    hostDeviceId = _deviceId,
                    hostName = state.hostName.trim(),
                    maxPlayers = state.maxPlayers,
                    questionsPerPlayer = state.questionsPerPlayer,
                    status = GameStatus.WAITING.name,
                    players = listOf(hostPlayer),
                    questions = emptyList()
                )

                // Firestore'a kaydet
                db.collection(GROUP_GAMES)
                    .document(gameCode)
                    .set(game.toMap())
                    .await()

                Log.d(TAG, "Oyun oluşturuldu: $gameCode")

                _createGameState.value = _createGameState.value.copy(
                    isCreating = false,
                    createdGameCode = gameCode
                )

                _currentGameCode.value = gameCode
                onSuccess(gameCode)

            } catch (e: Exception) {
                Log.e(TAG, "Oyun oluşturma hatası", e)
                _createGameState.value = _createGameState.value.copy(
                    isCreating = false,
                    error = "Oyun oluşturulamadı: ${e.localizedMessage}"
                )
                handleException(e)
            }
        }
    }

    // ============== OYUNA KATILMA ==============

    fun joinGame(gameCode: String, playerName: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        if (!DeviceIdHelper.isValidGameCode(gameCode)) {
            onError("Geçersiz oyun kodu")
            return
        }

        if (!DeviceIdHelper.isValidPlayerName(playerName)) {
            onError("Lütfen geçerli bir isim girin (2-20 karakter)")
            return
        }

        viewModelScope.launch {
            try {
                val gameDoc = db.collection(GROUP_GAMES).document(gameCode.uppercase()).get().await()

                if (!gameDoc.exists()) {
                    onError("Oyun bulunamadı")
                    return@launch
                }

                val game = parseGameDocument(gameDoc)

                if (game == null) {
                    onError("Oyun verisi okunamadı")
                    return@launch
                }

                // Oyun durumunu kontrol et
                if (game.status != GameStatus.WAITING.name) {
                    onError("Bu oyun zaten başlamış")
                    return@launch
                }

                // Oyuncu zaten oyunda mı?
                if (game.isPlayerInGame(_deviceId)) {
                    // Zaten oyunda, direkt bekleme odasına yönlendir
                    _currentGameCode.value = gameCode.uppercase()
                    onSuccess()
                    return@launch
                }

                // Oyuncu sayısı kontrolü
                if (game.players.size >= game.maxPlayers) {
                    onError("Oyun dolu (${game.maxPlayers} oyuncu)")
                    return@launch
                }

                // Yeni oyuncu oluştur
                val newPlayer = GroupPlayer(
                    deviceId = _deviceId,
                    name = playerName.trim(),
                    questionsAdded = 0,
                    isHost = false,
                    isReady = false
                )

                // Oyuncuyu ekle
                db.collection(GROUP_GAMES)
                    .document(gameCode.uppercase())
                    .update("players", FieldValue.arrayUnion(newPlayer.toMap()))
                    .await()

                Log.d(TAG, "Oyuna katıldı: $gameCode")

                _currentGameCode.value = gameCode.uppercase()
                onSuccess()

            } catch (e: Exception) {
                Log.e(TAG, "Oyuna katılma hatası", e)
                onError("Oyuna katılınamadı: ${e.localizedMessage}")
                handleException(e)
            }
        }
    }

    // ============== BEKLEME ODASI ==============

    fun startListeningToGame(gameCode: String) {
        stopListeningToGame()

        _waitingRoomState.value = WaitingRoomUiState(
            isLoading = true,
            currentDeviceId = _deviceId
        )

        gameListener = db.collection(GROUP_GAMES)
            .document(gameCode.uppercase())
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e(TAG, "Listener hatası", error)
                    _waitingRoomState.value = _waitingRoomState.value.copy(
                        isLoading = false,
                        error = "Bağlantı hatası: ${error.localizedMessage}"
                    )
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    val game = parseGameDocument(snapshot)
                    _waitingRoomState.value = _waitingRoomState.value.copy(
                        game = game,
                        isLoading = false,
                        error = null,
                        currentDeviceId = _deviceId
                    )

                    // Oyun durumu değiştiyse gameplay state'i de güncelle
                    if (game?.status == GameStatus.PLAYING.name) {
                        _gamePlayState.value = _gamePlayState.value.copy(
                            game = game,
                            isLoading = false
                        )
                    }

                    Log.d(TAG, "Oyun güncellendi: ${game?.players?.size} oyuncu")
                } else {
                    _waitingRoomState.value = _waitingRoomState.value.copy(
                        isLoading = false,
                        error = "Oyun bulunamadı"
                    )
                }
            }

        _currentGameCode.value = gameCode.uppercase()
    }

    fun stopListeningToGame() {
        gameListener?.remove()
        gameListener = null
    }

    // ============== SORU EKLEME ==============

    fun updateCurrentQuestion(question: String) {
        _addQuestionsState.value = _addQuestionsState.value.copy(currentQuestion = question)
    }

    fun updateCurrentType(type: QuestionType) {
        _addQuestionsState.value = _addQuestionsState.value.copy(currentType = type)
    }

    fun updateCurrentDifficulty(difficulty: DifficultyLevel) {
        _addQuestionsState.value = _addQuestionsState.value.copy(currentDifficulty = difficulty)
    }

    fun setRequiredQuestions(count: Int) {
        _addQuestionsState.value = _addQuestionsState.value.copy(requiredQuestions = count)
    }

    fun addQuestion() {
        val state = _addQuestionsState.value

        if (state.currentQuestion.trim().length < 5) {
            _addQuestionsState.value = state.copy(error = "Soru en az 5 karakter olmalı")
            return
        }

        // Mevcut oyuncunun adını al
        val playerName = _waitingRoomState.value.game?.getPlayerByDeviceId(_deviceId)?.name ?: "Anonim"

        val newQuestion = GroupQuestion(
            id = DeviceIdHelper.generateQuestionId(),
            question = state.currentQuestion.trim(),
            type = state.currentType.name,
            difficulty = state.currentDifficulty.name,
            addedBy = _deviceId,
            addedByName = playerName
        )

        _addQuestionsState.value = state.copy(
            addedQuestions = state.addedQuestions + newQuestion,
            currentQuestion = "",
            error = null
        )
    }

    fun removeQuestion(questionId: String) {
        val state = _addQuestionsState.value
        _addQuestionsState.value = state.copy(
            addedQuestions = state.addedQuestions.filter { it.id != questionId }
        )
    }

    fun saveQuestionsAndMarkReady(gameCode: String, onSuccess: () -> Unit) {
        val state = _addQuestionsState.value

        if (state.addedQuestions.size < state.requiredQuestions) {
            _addQuestionsState.value = state.copy(
                error = "${state.requiredQuestions} soru eklemeniz gerekiyor"
            )
            return
        }

        viewModelScope.launch {
            _addQuestionsState.value = state.copy(isSaving = true, error = null)

            try {
                val gameDoc = db.collection(GROUP_GAMES).document(gameCode).get().await()
                val game = parseGameDocument(gameDoc)

                if (game == null) {
                    _addQuestionsState.value = state.copy(isSaving = false, error = "Oyun bulunamadı")
                    return@launch
                }

                // Mevcut soruları al ve yenilerini ekle
                val updatedQuestions = game.questions + state.addedQuestions

                // Oyuncuyu güncelle (isReady = true, questionsAdded güncelle)
                val updatedPlayers = game.players.map { player ->
                    if (player.deviceId == _deviceId) {
                        player.copy(
                            questionsAdded = state.addedQuestions.size,
                            isReady = true
                        )
                    } else {
                        player
                    }
                }

                // Firestore güncelle
                db.collection(GROUP_GAMES)
                    .document(gameCode)
                    .update(
                        mapOf(
                            "questions" to updatedQuestions.map { it.toMap() },
                            "players" to updatedPlayers.map { it.toMap() },
                            "updatedAt" to System.currentTimeMillis()
                        )
                    )
                    .await()

                Log.d(TAG, "Sorular kaydedildi: ${state.addedQuestions.size}")

                _addQuestionsState.value = _addQuestionsState.value.copy(isSaving = false)
                onSuccess()

            } catch (e: Exception) {
                Log.e(TAG, "Soru kaydetme hatası", e)
                _addQuestionsState.value = _addQuestionsState.value.copy(
                    isSaving = false,
                    error = "Sorular kaydedilemedi: ${e.localizedMessage}"
                )
                handleException(e)
            }
        }
    }

    fun resetAddQuestionsState() {
        _addQuestionsState.value = AddQuestionsUiState()
    }

    // ============== OYUN BAŞLATMA (Host) ==============

    fun startGame(gameCode: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val gameDoc = db.collection(GROUP_GAMES).document(gameCode).get().await()
                val game = parseGameDocument(gameDoc)

                if (game == null) {
                    onError("Oyun bulunamadı")
                    return@launch
                }

                // Host kontrolü
                if (game.hostDeviceId != _deviceId) {
                    onError("Sadece oyun sahibi oyunu başlatabilir")
                    return@launch
                }

                // Oyuncu kontrolü
                if (!game.areAllPlayersReady()) {
                    onError("Tüm oyuncular hazır değil")
                    return@launch
                }

                if (game.questions.isEmpty()) {
                    onError("Hiç soru eklenmemiş")
                    return@launch
                }

                // Soruları karıştır
                val shuffledQuestions = game.questions.shuffled()

                // Oyunu başlat
                db.collection(GROUP_GAMES)
                    .document(gameCode)
                    .update(
                        mapOf(
                            "status" to GameStatus.PLAYING.name,
                            "questions" to shuffledQuestions.map { it.toMap() },
                            "currentPlayerIndex" to 0,
                            "currentQuestionIndex" to 0,
                            "updatedAt" to System.currentTimeMillis()
                        )
                    )
                    .await()

                Log.d(TAG, "Oyun başlatıldı: $gameCode")
                onSuccess()

            } catch (e: Exception) {
                Log.e(TAG, "Oyun başlatma hatası", e)
                onError("Oyun başlatılamadı: ${e.localizedMessage}")
                handleException(e)
            }
        }
    }

    // ============== OYUN OYNAMA ==============

    fun getCurrentQuestion(): GroupQuestion? {
        val game = _gamePlayState.value.game ?: return null
        return game.questions.getOrNull(game.currentQuestionIndex)
    }

    fun getCurrentPlayer(): GroupPlayer? {
        val game = _gamePlayState.value.game ?: return null
        return game.players.getOrNull(game.currentPlayerIndex)
    }

    fun getPlayerNames(): List<String> {
        return _gamePlayState.value.game?.players?.map { it.name } ?: emptyList()
    }

    fun moveToNextQuestion(gameCode: String, selectedPlayerIndex: Int? = null) {
        viewModelScope.launch {
            try {
                val gameDoc = db.collection(GROUP_GAMES).document(gameCode).get().await()
                val game = parseGameDocument(gameDoc)

                if (game == null) return@launch

                // Mevcut soruyu listeden sil (yığından at)
                val remainingQuestions = game.questions.filterIndexed { index, _ ->
                    index != game.currentQuestionIndex
                }

                val nextPlayerIndex = selectedPlayerIndex
                    ?: ((game.currentPlayerIndex + 1) % game.players.size)

                // Oyun bitti mi kontrol et (kalan soru yoksa)
                if (remainingQuestions.isEmpty()) {
                    // Oyunu bitir
                    db.collection(GROUP_GAMES)
                        .document(gameCode)
                        .update(
                            mapOf(
                                "status" to GameStatus.FINISHED.name,
                                "questions" to emptyList<Map<String, Any>>(),
                                "updatedAt" to System.currentTimeMillis()
                            )
                        )
                        .await()

                    _gamePlayState.value = _gamePlayState.value.copy(isGameFinished = true)
                    return@launch
                }

                // Sonraki soruya geç (index 0'da kal çünkü mevcut soru silindi)
                db.collection(GROUP_GAMES)
                    .document(gameCode)
                    .update(
                        mapOf(
                            "currentQuestionIndex" to 0, // Her zaman 0, çünkü kullanılan soru siliniyor
                            "currentPlayerIndex" to nextPlayerIndex,
                            "questions" to remainingQuestions.map { it.toMap() },
                            "updatedAt" to System.currentTimeMillis()
                        )
                    )
                    .await()

                Log.d(TAG, "Soru silindi, kalan: ${remainingQuestions.size}, Oyuncu: $nextPlayerIndex")

            } catch (e: Exception) {
                Log.e(TAG, "Sonraki soruya geçme hatası", e)
                handleException(e)
            }
        }
    }

    fun setSelectedPlayer(playerIndex: Int, gameCode: String) {
        viewModelScope.launch {
            try {
                db.collection(GROUP_GAMES)
                    .document(gameCode)
                    .update("currentPlayerIndex", playerIndex)
                    .await()
            } catch (e: Exception) {
                Log.e(TAG, "Oyuncu seçme hatası", e)
                handleException(e)
            }
        }
    }

    fun toggleShowQuestionAuthor(gameCode: String) {
        viewModelScope.launch {
            try {
                val current = _waitingRoomState.value.game?.showQuestionAuthor ?: true
                db.collection(GROUP_GAMES)
                    .document(gameCode)
                    .update("showQuestionAuthor", !current)
                    .await()
            } catch (e: Exception) {
                Log.e(TAG, "Yazar gösterme toggle hatası", e)
                handleException(e)
            }
        }
    }

    // ============== OYUNDAN AYRILMA ==============

    fun leaveGame(gameCode: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                val gameDoc = db.collection(GROUP_GAMES).document(gameCode).get().await()
                val game = parseGameDocument(gameDoc)

                if (game == null) {
                    onSuccess()
                    return@launch
                }

                // Oyuncuyu bul
                val playerToRemove = game.players.find { it.deviceId == _deviceId }

                if (playerToRemove != null) {
                    // Host ayrılıyorsa oyunu sil
                    if (playerToRemove.isHost) {
                        db.collection(GROUP_GAMES)
                            .document(gameCode)
                            .delete()
                            .await()
                        Log.d(TAG, "Host ayrıldı, oyun silindi: $gameCode")
                    } else {
                        // Normal oyuncu ayrılıyor
                        val updatedPlayers = game.players.filter { it.deviceId != _deviceId }
                        val updatedQuestions = game.questions.filter { it.addedBy != _deviceId }

                        db.collection(GROUP_GAMES)
                            .document(gameCode)
                            .update(
                                mapOf(
                                    "players" to updatedPlayers.map { it.toMap() },
                                    "questions" to updatedQuestions.map { it.toMap() },
                                    "updatedAt" to System.currentTimeMillis()
                                )
                            )
                            .await()
                        Log.d(TAG, "Oyuncu ayrıldı: $gameCode")
                    }
                }

                stopListeningToGame()
                _currentGameCode.value = null
                onSuccess()

            } catch (e: Exception) {
                Log.e(TAG, "Oyundan ayrılma hatası", e)
                handleException(e)
                onSuccess() // Hata olsa bile çık
            }
        }
    }

    // ============== YARDIMCI METODLAR ==============

    @Suppress("UNCHECKED_CAST")
    private fun parseGameDocument(document: com.google.firebase.firestore.DocumentSnapshot): GroupGame? {
        return try {
            val data = document.data ?: return null

            val playersData = data["players"] as? List<Map<String, Any>> ?: emptyList()
            val players = playersData.map { playerMap ->
                GroupPlayer(
                    deviceId = playerMap["deviceId"] as? String ?: "",
                    name = playerMap["name"] as? String ?: "",
                    questionsAdded = (playerMap["questionsAdded"] as? Long)?.toInt() ?: 0,
                    isHost = playerMap["isHost"] as? Boolean ?: false,
                    isReady = playerMap["isReady"] as? Boolean ?: false,
                    joinedAt = playerMap["joinedAt"] as? Long ?: 0
                )
            }

            val questionsData = data["questions"] as? List<Map<String, Any>> ?: emptyList()
            val questions = questionsData.map { questionMap ->
                GroupQuestion(
                    id = questionMap["id"] as? String ?: "",
                    question = questionMap["question"] as? String ?: "",
                    type = questionMap["type"] as? String ?: QuestionType.TRUTH.name,
                    difficulty = questionMap["difficulty"] as? String ?: DifficultyLevel.MEDIUM.name,
                    addedBy = questionMap["addedBy"] as? String ?: "",
                    addedByName = questionMap["addedByName"] as? String ?: "",
                    isUsed = questionMap["isUsed"] as? Boolean ?: false,
                    createdAt = questionMap["createdAt"] as? Long ?: 0
                )
            }

            GroupGame(
                gameId = data["gameId"] as? String ?: "",
                gameCode = data["gameCode"] as? String ?: "",
                hostDeviceId = data["hostDeviceId"] as? String ?: "",
                hostName = data["hostName"] as? String ?: "",
                maxPlayers = (data["maxPlayers"] as? Long)?.toInt() ?: 4,
                questionsPerPlayer = (data["questionsPerPlayer"] as? Long)?.toInt() ?: 5,
                status = data["status"] as? String ?: GameStatus.WAITING.name,
                currentPlayerIndex = (data["currentPlayerIndex"] as? Long)?.toInt() ?: 0,
                currentQuestionIndex = (data["currentQuestionIndex"] as? Long)?.toInt() ?: 0,
                showQuestionAuthor = data["showQuestionAuthor"] as? Boolean ?: true,
                players = players,
                questions = questions,
                createdAt = data["createdAt"] as? Long ?: 0,
                updatedAt = data["updatedAt"] as? Long ?: 0
            )
        } catch (e: Exception) {
            Log.e(TAG, "Oyun parse hatası", e)
            null
        }
    }

    fun isCurrentDeviceHost(): Boolean {
        return _waitingRoomState.value.game?.hostDeviceId == _deviceId
    }

    fun isCurrentPlayerReady(): Boolean {
        return _waitingRoomState.value.game?.getPlayerByDeviceId(_deviceId)?.isReady == true
    }

    fun resetCreateGameState() {
        _createGameState.value = CreateGameUiState()
    }

    fun clearError() {
        _createGameState.value = _createGameState.value.copy(error = null)
        _waitingRoomState.value = _waitingRoomState.value.copy(error = null)
        _addQuestionsState.value = _addQuestionsState.value.copy(error = null)
        _gamePlayState.value = _gamePlayState.value.copy(error = null)
    }

    override fun onCleared() {
        super.onCleared()
        stopListeningToGame()
    }
}
