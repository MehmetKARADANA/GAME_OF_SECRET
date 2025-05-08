package com.mobile.gameofsecret.viewmodels

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.mobile.gameofsecret.data.GAMERS
import com.mobile.gameofsecret.data.model.Gamer
import com.mobile.gameofsecret.data.roomdb.GamerDatabase
import com.mobile.gameofsecret.data.roomdb.getDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GamerViewModel(application: Application) : BaseViewModel(application) {

    /*   private val db = Room.databaseBuilder(
           getApplication(),
           GamerDatabase::class.java,
           GAMERS
       ).build()*/
    private val db = getDatabase(application)

    private val _gamerList = MutableStateFlow<List<Gamer>>(emptyList())
    val gamerList: StateFlow<List<Gamer>> = _gamerList.asStateFlow()
    val currentGamer = mutableStateOf<Gamer?>(null)
    val selectedGamer = mutableStateOf<Gamer>(Gamer(""))

    init {
        //  getGamerList()
    }

    private val gamerDao = db.gamerDao()

    private fun setGamer() {
        if (_gamerList.value.isNotEmpty()) {
            currentGamer.value = _gamerList.value.first()
        }
    }

    fun nextPlayer() {
        val index = _gamerList.value.indexOf(currentGamer.value)
        if (index != -1) {
            val nextIndex = (index + 1) % _gamerList.value.size
            currentGamer.value = _gamerList.value[nextIndex]
        }
    }

    fun getGamerList() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val list = gamerDao.getGamerNameAndId()
                withContext(Dispatchers.Main) {
                    _gamerList.value = list
                    setGamer()
                }

            } catch (e: Exception) {
                e.printStackTrace()
                handleException(e)
                //handle
            }
        }

    }

    fun resetGamers(userFields: List<String>, onComplete: () -> Unit) {

        viewModelScope.launch(Dispatchers.IO) {
            try {
                gamerDao.deleteAll()

                userFields.forEach { gamerName ->
                    gamerDao.insert(Gamer(gamerName))
                }

                _gamerList.value = gamerDao.getGamerNameAndId()

                withContext(Dispatchers.Main) {
                    setGamer()
                    //unutma uıyı etkileyen şeyler main threadde
                    onComplete()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("GamerViewModel", "ResetGamers hata oluştu")
                //hata mesajları çevirileri ekle
                handleException(customMessage = "Bir hata oluştu")
            }
        }
    }

    fun getGamer(id: Int) {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                selectedGamer.value = gamerDao.getGamerById(id)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            handleException(e)
        }
    }

    fun saveGamer(gamer: Gamer) {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                gamerDao.insert(gamer)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            handleException(e)
        }
    }

    fun deleteGamer(gamer: Gamer) {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                gamerDao.delete(gamer)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            handleException(e)
        }
    }

    fun updateGamer(gamer: Gamer) {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                gamerDao.update(gamer)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            handleException(e)
        }
    }

    fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                gamerDao.deleteAll()
            } catch (e: Exception) {
                e.printStackTrace()
                handleException(e)
            }
        }

    }

}