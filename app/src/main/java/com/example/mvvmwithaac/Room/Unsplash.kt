package com.example.mvvmwithaac.Room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "unsplash")
data class Unsplash(
    @PrimaryKey(autoGenerate = true)
    var un_id: Long?,

    @ColumnInfo(name = "un_url")
    var un_url: String
) {
    constructor() : this(null, "")
}