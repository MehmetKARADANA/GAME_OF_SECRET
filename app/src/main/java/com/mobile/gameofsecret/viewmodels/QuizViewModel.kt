package com.mobile.gameofsecret.viewmodels

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.mobile.gameofsecret.data.QUESTIONS
import com.mobile.gameofsecret.data.model.Question
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
               val snapshot = db.collection(QUESTIONS)
                    //.orderBy("id")
                   // .limit(20)
                    .get()
                    .await()


                val allQuestions = snapshot.documents.mapNotNull {
                    it.toObject(Question::class.java)
                }.toMutableList()

                val randomQuestions = allQuestions.shuffled().take(10)

                _questionCache.value = randomQuestions

                if (_questionCache.value.isNotEmpty() && _question.value == null) {
                    val nextQuestion = _questionCache.value.first()
                    _question.value = nextQuestion
                    _questionCache.value = _questionCache.value.drop(1)
                }

                Log.d("Quiz", "fetchRandomQuestion: 10 soru alındı")
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
            fetchRandomQuestion()
        } else {
            val nextQuestion = _questionCache.value.firstOrNull()
            _question.value = nextQuestion
            _questionCache.value = _questionCache.value.drop(1)
            Log.d("Quiz", "getRandomQuestion: Soru seçildi ve cache güncellendi")
        }
    }


}