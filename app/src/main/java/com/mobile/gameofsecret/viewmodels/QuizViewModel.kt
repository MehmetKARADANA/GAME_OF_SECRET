package com.mobile.gameofsecret.viewmodels

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.mobile.gameofsecret.data.QUESTIONS

import com.mobile.gameofsecret.data.model.Question

import com.mobile.gameofsecret.data.model.Questions
import com.mobile.gameofsecret.data.model.Translation
import com.mobile.gameofsecret.localization.LanguageManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlin.random.Random


class QuizViewModel(application: Application) : BaseViewModel(application) {
    private val db = Firebase.firestore
    private val _questionCache = MutableStateFlow<List<Question>>(emptyList())

    private val _question = MutableStateFlow<Question?>(null)
    val question: StateFlow<Question?> = _question

    init {
        getRandomQuestion()
    }
    private fun fetchRandomQuestion() {
        viewModelScope.launch {
            try {
                val currentLanguage = LanguageManager.getLanguage(getApplication())

                val snapshot = db.collection("questions_truth").get().await()

                if (snapshot.isEmpty) {
                    Log.d("Quiz", "Firestore'dan veri alınamadı. Koleksiyon boş.")
                    return@launch
                }

                val questionList = mutableListOf<Question>()
                Log.d("Quiz", "fetchRandomQuestion: ${questionList.size} soru alındı")
                for (document in snapshot.documents) {
                    // Belgeden dil verisini al
                    val questionText = document.getString(currentLanguage)

                    if (questionText != null) {
                        val question = Question(
                            id = document.getLong("id")?.toInt() ?: 0,
                            question = questionText
                        )
                        questionList.add(question)
                    } else {
                        Log.d("Quiz", "Translation for $currentLanguage not found for document ${document.id}")
                    }
                }

                val randomQuestions = questionList.shuffled().take(10)
                _questionCache.value = randomQuestions

                if (_questionCache.value.isNotEmpty() && _question.value == null) {
                    val nextQuestion = _questionCache.value.first()
                    _question.value = nextQuestion
                    _questionCache.value = _questionCache.value.drop(1)
                }


            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("Quiz", "Hata: ${e.message}")
                handleException(e)
            }
        }
    }


    @SuppressLint("NewApi")
    fun getRandomQuestion() {
        if (_questionCache.value.isEmpty()) {
            Log.d("Quiz", "getRandomQuestion: FetchRandomquestion() çağrılıyor.")
            fetchRandomQuestion()
        } else {
            val nextQuestion = _questionCache.value.firstOrNull()
            _question.value = nextQuestion
            _questionCache.value = _questionCache.value.drop(1)
            Log.d("Quiz", "getRandomQuestion: Soru seçildi ve cache güncellendi")
        }
    }


}