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
import kotlinx.coroutines.launch

class GamerViewModel(application: Application) : AndroidViewModel(application) {

    private val db = Room.databaseBuilder(
        getApplication(),
        GamerDatabase::class.java,
        GAMERS
    ).build()

    val gamerList = mutableStateOf<List<Gamer>>(listOf())
    val selectedGamer = mutableStateOf<Gamer>(Gamer(""))

    private val gamerDao = db.gamerDao()

    fun getItemList() {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                gamerList.value = gamerDao.getGamerNameAndId()
            }
        } catch (e: Exception) {
            e.printStackTrace()
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
        }
    }

    fun saveGamer(gamer: Gamer) {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                gamerDao.insert(gamer)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun deleteGamer(gamer: Gamer) {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                gamerDao.delete(gamer)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun updateGamer(gamer: Gamer) {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                gamerDao.update(gamer)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun deleteAll(){
        try {
            viewModelScope.launch (Dispatchers.IO){
                gamerDao.deleteAll()
            }
        }catch (e : Exception){
            e.printStackTrace()
        }
    }

}