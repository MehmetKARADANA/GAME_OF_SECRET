package com.mobile.gameofsecret.viewmodels

import android.app.Application
import android.os.Message
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import com.mobile.gameofsecret.data.Event
import kotlinx.coroutines.flow.StateFlow
import java.lang.Exception


open class BaseViewModel(application: Application) : AndroidViewModel(application) {

    private val _errorMessage = mutableStateOf<Event<String>?>(null)
    val errorMessage : State<Event<String>?> = _errorMessage

    protected fun handleException(exception: Exception?=null,customMessage: String=""){
        Log.e("BaseViewModel", "Hata: ", exception)
        exception?.printStackTrace()
        val errorMsg =exception?.localizedMessage ?:""
        val message = if (customMessage.isEmpty()) errorMsg else customMessage
        _errorMessage.value=Event(message)
    }


}