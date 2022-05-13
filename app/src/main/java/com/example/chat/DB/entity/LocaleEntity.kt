package com.example.chat.DB.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LocaleEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "locale")
    var locale: Int
)