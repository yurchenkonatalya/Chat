package com.example.chat.DB.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id:Long,

    @ColumnInfo(name = "user_name")
    var user_name: String,

    @ColumnInfo(name = "user_surname")
    var user_surname: String,

    @ColumnInfo(name = "user_mail")
    var user_mail: String,

    @ColumnInfo(name = "user_phone")
    var user_phone: String,

    @ColumnInfo(name = "user_nickname")
    var user_nickname: String,

    @ColumnInfo(name = "user_password")
    var user_password: String,

    @ColumnInfo(name = "user_birthday")
    var user_birthday: String,

    @ColumnInfo(name = "user_last_active")
    var user_last_active: String,

    @ColumnInfo(name = "user_photo")
    var user_photo: String
)