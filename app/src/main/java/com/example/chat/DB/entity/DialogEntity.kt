package com.example.chat.DB.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DialogEntity(
    @ColumnInfo(name = "id")
    var id: Long,
    var date: String,
    var text: String,
    var info: String?,
    var unread_msg_count: Int,

    @PrimaryKey(autoGenerate = false)
    var opponent_id: Long,
    var opponent_name: String,
    var opponent_surname: String,
    var opponent_nickname: String,
    var opponent_photo: String,
    var opponent_last_active: String,
    var responseIndex: Int
)