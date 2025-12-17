package com.mobile.gameofsecret.data.model

/**
 * Grup oyunu için Firestore data modelleri
 */

// Oyun durumu enum'u
enum class GameStatus {
    WAITING,    // Oyuncular bekleniyor
    PLAYING,    // Oyun devam ediyor
    FINISHED    // Oyun bitti
}

// Zorluk seviyesi enum'u
enum class DifficultyLevel {
    EASY,       // Kolay
    MEDIUM,     // Orta
    HARD        // Zor
}

// Soru tipi enum'u
enum class QuestionType {
    TRUTH,      // Doğruluk
    DARE        // Cesaret
}

/**
 * Oyuncu modeli - Her oyuncu için
 */
data class GroupPlayer(
    val deviceId: String = "",
    val name: String = "",
    val questionsAdded: Int = 0,
    val isHost: Boolean = false,
    val isReady: Boolean = false,
    val joinedAt: Long = System.currentTimeMillis()
) {
    // Firestore için boş constructor gerekli
    constructor() : this("", "", 0, false, false, 0)

    fun toMap(): Map<String, Any> = mapOf(
        "deviceId" to deviceId,
        "name" to name,
        "questionsAdded" to questionsAdded,
        "isHost" to isHost,
        "isReady" to isReady,
        "joinedAt" to joinedAt
    )
}

/**
 * Grup sorusu modeli - Oyuncuların eklediği sorular
 */
data class GroupQuestion(
    val id: String = "",
    val question: String = "",
    val type: String = QuestionType.TRUTH.name,      // TRUTH veya DARE
    val difficulty: String = DifficultyLevel.MEDIUM.name, // EASY, MEDIUM, HARD
    val addedBy: String = "",           // deviceId
    val addedByName: String = "",       // Oyuncu adı
    val isUsed: Boolean = false,        // Kullanıldı mı?
    val createdAt: Long = System.currentTimeMillis()
) {
    constructor() : this("", "", QuestionType.TRUTH.name, DifficultyLevel.MEDIUM.name, "", "", false, 0)

    fun toMap(): Map<String, Any> = mapOf(
        "id" to id,
        "question" to question,
        "type" to type,
        "difficulty" to difficulty,
        "addedBy" to addedBy,
        "addedByName" to addedByName,
        "isUsed" to isUsed,
        "createdAt" to createdAt
    )
}

/**
 * Ana oyun modeli - Firestore'da "group_games" koleksiyonunda
 */
data class GroupGame(
    val gameId: String = "",
    val gameCode: String = "",          // 6 karakterlik benzersiz kod (ABC123)
    val hostDeviceId: String = "",
    val hostName: String = "",
    val maxPlayers: Int = 4,
    val questionsPerPlayer: Int = 5,
    val status: String = GameStatus.WAITING.name,
    val currentPlayerIndex: Int = 0,
    val currentQuestionIndex: Int = 0,
    val showQuestionAuthor: Boolean = true,  // Soru yazarını göster/gizle
    val players: List<GroupPlayer> = emptyList(),
    val questions: List<GroupQuestion> = emptyList(),
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    constructor() : this("", "", "", "", 4, 5, GameStatus.WAITING.name, 0, 0, true, emptyList(), emptyList(), 0, 0)

    fun toMap(): Map<String, Any> = mapOf(
        "gameId" to gameId,
        "gameCode" to gameCode,
        "hostDeviceId" to hostDeviceId,
        "hostName" to hostName,
        "maxPlayers" to maxPlayers,
        "questionsPerPlayer" to questionsPerPlayer,
        "status" to status,
        "currentPlayerIndex" to currentPlayerIndex,
        "currentQuestionIndex" to currentQuestionIndex,
        "showQuestionAuthor" to showQuestionAuthor,
        "players" to players.map { it.toMap() },
        "questions" to questions.map { it.toMap() },
        "createdAt" to createdAt,
        "updatedAt" to updatedAt
    )

    // Yardımcı fonksiyonlar
    fun isPlayerInGame(deviceId: String): Boolean =
        players.any { it.deviceId == deviceId }

    fun getPlayerByDeviceId(deviceId: String): GroupPlayer? =
        players.find { it.deviceId == deviceId }

    fun areAllPlayersReady(): Boolean =
        players.size >= 2 && players.all { it.isReady }

    fun getReadyPlayersCount(): Int =
        players.count { it.isReady }

    fun canStartGame(): Boolean =
        players.size >= 2 && areAllPlayersReady() && questions.isNotEmpty()

    fun getShareableLink(): String =
        "https://gameofsecret.app/g/$gameCode"

    fun getDeepLink(): String =
        "gameofsecret://join?gameId=$gameCode"
}

/**
 * Oyun oluşturma için UI state
 */
data class CreateGameUiState(
    val hostName: String = "",
    val maxPlayers: Int = 4,
    val questionsPerPlayer: Int = 5,
    val isCreating: Boolean = false,
    val error: String? = null,
    val createdGameCode: String? = null
)

/**
 * Bekleme odası için UI state
 */
data class WaitingRoomUiState(
    val game: GroupGame? = null,
    val isLoading: Boolean = true,
    val error: String? = null,
    val currentDeviceId: String = ""
)

/**
 * Soru ekleme için UI state
 */
data class AddQuestionsUiState(
    val currentQuestion: String = "",
    val currentType: QuestionType = QuestionType.TRUTH,
    val currentDifficulty: DifficultyLevel = DifficultyLevel.MEDIUM,
    val addedQuestions: List<GroupQuestion> = emptyList(),
    val requiredQuestions: Int = 5,
    val isSaving: Boolean = false,
    val error: String? = null
)

/**
 * Grup oyunu oynama için UI state
 */
data class GroupGamePlayUiState(
    val game: GroupGame? = null,
    val currentQuestion: GroupQuestion? = null,
    val currentPlayer: GroupPlayer? = null,
    val isSpinning: Boolean = false,
    val showResult: Boolean = false,
    val isLoading: Boolean = true,
    val error: String? = null,
    val isGameFinished: Boolean = false
)
