package com.mobile.gameofsecret.viewmodels

import android.app.Application
import androidx.room.Room
import com.mobile.gameofsecret.data.GAMERS

import com.mobile.gameofsecret.data.roomdb.GamerDatabase

class TaskViewModel(application: Application) : BaseViewModel(application) {
    private val db = Room.databaseBuilder(
        getApplication(),
        GamerDatabase::class.java,
        GAMERS
    ).build()

  //  private val taskDao =db.taskDao()

}