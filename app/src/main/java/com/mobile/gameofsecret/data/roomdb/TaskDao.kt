package com.mobile.gameofsecret.data.roomdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.mobile.gameofsecret.data.model.Gamer
import com.mobile.gameofsecret.data.model.Task

@Dao
interface TaskDao {
    @Query("SELECT task,id FROM task")
    fun getTaskAndId() : List<Task>

    @Query("SELECT * From task WHERE id = :id")
    fun getTaskById(id :Int) : Task

    @Insert
    suspend fun insert(task : Task)

    @Delete
    suspend fun delete(task: Task)

    @Query("DELETE FROM task")
    fun deleteAll()

}