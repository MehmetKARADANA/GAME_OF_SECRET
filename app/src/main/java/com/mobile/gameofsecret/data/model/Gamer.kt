package com.mobile.gameofsecret.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
class Gamer(
    var name: String,
) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var id = 0
}