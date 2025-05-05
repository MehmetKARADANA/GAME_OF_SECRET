package com.mobile.gameofsecret.data.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mobile.gameofsecret.data.model.Gamer
import com.mobile.gameofsecret.data.model.Task

@Database(entities = [Gamer::class/*,Task::class*/], version = 1)
abstract class GamerDatabase : RoomDatabase(){
    abstract fun gamerDao() : GamerDao
  //  abstract fun taskDao() : TaskDao
}