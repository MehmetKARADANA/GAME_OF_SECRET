package com.mobile.gameofsecret.data.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mobile.gameofsecret.data.model.Gamer
import com.mobile.gameofsecret.data.model.Task

@Database(entities = [Gamer::class,Task::class], version = 1)
abstract class GamerDatabase : RoomDatabase(){
    abstract fun gamerDao() : GamerDao
    abstract fun taskDao() : TaskDao

}

@Volatile
private var INSTANCE: GamerDatabase? = null

fun getDatabase(context: Context): GamerDatabase {
    return INSTANCE ?: synchronized(context) {
        val instance = Room.databaseBuilder(
            context.applicationContext,
            GamerDatabase::class.java,
            "app_database"
        ).build()
        INSTANCE = instance
        instance
    }
}