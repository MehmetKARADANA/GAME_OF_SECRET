package com.mobile.gameofsecret.data.roomdb


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.mobile.gameofsecret.data.model.Gamer

@Dao
interface GamerDao{

    @Query("SELECT name,id FROM gamer")
    fun getGamerNameAndId() :List<Gamer>

    //by id
    @Query("SELECT * From gamer WHERE id = :id")
    fun getGamerById(id :Int) : Gamer

    @Insert
    suspend fun insert(gamer : Gamer)

    @Delete
    suspend fun delete(gamer: Gamer)

    @Update
    suspend fun update(gamer: Gamer)

    @Query("DELETE FROM gamer")
    fun deleteAll()

}