package com.example.chat.DB.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity //(foreignKeys = arrayOf(ForeignKey( entity = UserEntity::class, parentColumns = arrayOf("id"), childColumns = arrayOf("id"), onDelete = ForeignKey.RESTRICT ) ))
data class AuthorizedUserEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id:Long,
)