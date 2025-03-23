package com.mobile.gameofsecret.data.roomdb

import android.content.ClipData.Item
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.mobile.gameofsecret.data.model.Gamer

@Dao
interface GamerDao{

    @Query("SELECT name,id FROM Gamer")
    fun getItemNameAndId() :List<Gamer>

    //by id
    @Query("SELECT * From Gamer WHERE id = :id")
    fun getItemById(id :Int) : Item

    @Insert
    suspend fun insert(gamer : Gamer)

    @Delete
    suspend fun delete(gamer: Gamer)

}