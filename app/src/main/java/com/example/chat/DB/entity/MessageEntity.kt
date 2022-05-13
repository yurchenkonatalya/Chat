package com.example.chat.DB.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MessageEntity(
    @PrimaryKey(autoGenerate = false)
    var id: Long,
    var date: String,
    var id_user_source: Long,
    var id_user_destination: Long,
    var text: String,
    var info: String?,
    var is_read: Int
)