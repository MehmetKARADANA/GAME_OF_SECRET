package com.mobile.gameofsecret.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Gamer(
    var name: String,
    var truth: Int? = 0,
    var dare: Int? = 0,
    var downVote: Int? = 0
) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var id = 0
}

@Entity
data class Task(var task: String) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var id = 0
}