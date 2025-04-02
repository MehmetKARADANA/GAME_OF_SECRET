package com.mobile.gameofsecret.viewmodels

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.mobile.gameofsecret.data.GAMERS
import com.mobile.gameofsecret.data.model.Gamer
import com.mobile.gameofsecret.data.roomdb.GamerDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GamerViewModel(application: Application) : BaseViewModel(application) {

    private val db = Room.databaseBuilder(
        getApplication(),
        GamerDatabase::class.java,
        GAMERS
    ).build()

    private val _gamerList = MutableStateFlow<List<Gamer>>(emptyList())
    val gamerList: StateFlow<List<Gamer>> = _gamerList.asStateFlow()
    val currentGamer = mutableStateOf<Gamer?>(null)
    val selectedGamer = mutableStateOf<Gamer>(Gamer(""))

    init {
        getGamerList()
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
        try {
            viewModelScope.launch(Dispatchers.IO) {
                _gamerList.value = gamerDao.getGamerNameAndId()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            handleException(e)
            //handle
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
            setGamer()
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
        try {
            viewModelScope.launch(Dispatchers.IO) {
                gamerDao.deleteAll()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            handleException(e)
        }
    }

}