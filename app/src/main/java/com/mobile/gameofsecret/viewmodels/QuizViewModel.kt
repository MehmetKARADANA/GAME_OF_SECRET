package com.mobile.gameofsecret.viewmodels

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.mobile.gameofsecret.data.DARE
import com.mobile.gameofsecret.data.TRUTH
import com.mobile.gameofsecret.data.model.Question
import com.mobile.gameofsecret.localization.LanguageManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlin.random.Random


class QuizViewModel(application: Application) : BaseViewModel(application) {
    private val db = Firebase.firestore
    private val _dareQuestionCache = MutableStateFlow<List<Question>>(emptyList())
    private val _truthQuestionCache = MutableStateFlow<List<Question>>(emptyList())

    private val _dareQuestion = MutableStateFlow<Question?>(null)
    val dareQuestion: StateFlow<Question?> = _dareQuestion

    private val _truthQuestion = MutableStateFlow<Question?>(null)
    val truthQuestion: StateFlow<Question?> = _truthQuestion

    init {
        getRandomDareQuestion()
        getRandomTruthQuestion()
    }

    private fun fetchRandomTruthQuestion(){
        viewModelScope.launch {
            try {
                val currentLanguage = LanguageManager.getLanguage(getApplication())
              //  val randomId = (0..200).shuffled().take(20)

                val snapshot = db.collection(TRUTH).get().await()

                if (snapshot.isEmpty) {
                    Log.d("Quiz", "Firestore'dan veri alınamadı. Koleksiyon boş.")
                    return@launch
                }

                val questionList = mutableListOf<Question>()
                for (document in snapshot.documents) {
                    val questionText = document.get(currentLanguage)
                    //getstring bazen string değil anlıyor bu hatadan kurtulmak için
                    val questionTextStr = when (questionText) {
                        is String -> questionText
                        is Number -> questionText.toString()
                        else ->     "Bir Aksilik Oluştu(en,fr,tr)"
                    }
                    if (questionText != null) {
                        val question = Question(
                            id = document.getLong("id")?.toInt() ?: 0,
                            question = questionTextStr
                        )
                        questionList.add(question)
                    } else {
                        handleException(customMessage = "Bir hata oluştu")
                        Log.d("Quiz", "Translation for $currentLanguage not found for document ${document.id}")
                    }
                }
               /* for (document in snapshot.documents) {
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
                }*/

                Log.d("Quiz", "fetchRandomQuestion: ${questionList.size} soru alındı")
                val randomQuestions = questionList.shuffled().take(10)
                _truthQuestionCache.value = randomQuestions

                if (_truthQuestionCache.value.isNotEmpty() && _truthQuestion.value == null) {
                    val nextQuestion = _truthQuestionCache.value.first()
                    _truthQuestion.value = nextQuestion
                    _truthQuestionCache.value = _truthQuestionCache.value.drop(1)
                }


            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("Quiz", "Hata: ${e.message}")
                handleException(e)
            }
        }
    }
    @SuppressLint("NewApi")
    fun getRandomTruthQuestion(){
        if (_truthQuestionCache.value.isEmpty()) {
            Log.d("Quiz", "getRandomQuestion: FetchRandomquestion() çağrılıyor.")
            fetchRandomTruthQuestion()
        } else {
            val nextQuestion = _truthQuestionCache.value.firstOrNull()
            _truthQuestion.value = nextQuestion
            _truthQuestionCache.value = _truthQuestionCache.value.drop(1)
            Log.d("Quiz", "getRandomQuestion: Soru seçildi ve cache güncellendi")
        }
    }
    private fun fetchRandomDareQuestion() {
        viewModelScope.launch {
            try {
                val currentLanguage = LanguageManager.getLanguage(getApplication())

                val snapshot = db.collection(DARE).get().await()

                if (snapshot.isEmpty) {
                    Log.d("Quiz", "Firestore'dan veri alınamadı. Koleksiyon boş.")
                    return@launch
                }

                val questionList = mutableListOf<Question>()

                for (document in snapshot.documents) {
                    val questionText = document.get(currentLanguage)
                    //getstring bazen string değil anlıyor bu hatadan kurtulmak için
                    val questionTextStr = when (questionText) {
                        is String -> questionText
                        is Number -> questionText.toString()
                        else ->     "Bir Aksilik Oluştu(en,fr,tr)"
                    }
                    if (questionText != null) {
                        val question = Question(
                            id = document.getLong("id")?.toInt() ?: 0,
                            question = questionTextStr
                        )
                        questionList.add(question)
                    } else {
                        handleException(customMessage = "Bir hata oluştu")
                        Log.d("Quiz", "Translation for $currentLanguage not found for document ${document.id}")
                    }
                }
                Log.d("Quiz", "fetchRandomQuestion: ${questionList.size} soru alındı")
                val randomQuestions = questionList.shuffled().take(10)
                _dareQuestionCache.value = randomQuestions

                if (_dareQuestionCache.value.isNotEmpty() && _dareQuestion.value == null) {
                    val nextQuestion = _dareQuestionCache.value.first()
                    _dareQuestion.value = nextQuestion
                    _dareQuestionCache.value = _dareQuestionCache.value.drop(1)
                }


            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("Quiz", "Hata: ${e.message}")
                handleException(e)
            }
        }
    }


    @SuppressLint("NewApi")
    fun getRandomDareQuestion() {
        if (_dareQuestionCache.value.isEmpty()) {
            Log.d("Quiz", "getRandomQuestion: FetchRandomquestion() çağrılıyor.")
            fetchRandomDareQuestion()
        } else {
            val nextQuestion = _dareQuestionCache.value.firstOrNull()
            _dareQuestion.value = nextQuestion
            _dareQuestionCache.value = _dareQuestionCache.value.drop(1)
            Log.d("Quiz", "getRandomQuestion: Soru seçildi ve cache güncellendi")
        }
    }


}