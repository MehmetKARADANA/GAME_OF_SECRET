package com.mobile.gameofsecret.data.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mobile.gameofsecret.data.model.Gamer

@Database(entities = [Gamer::class], version = 1)
abstract class GamerDatabase : RoomDatabase(){
    abstract fun gamerDao() : GamerDao
}