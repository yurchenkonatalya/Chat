package com.example.chat.DB.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.chat.DB.entity.UserEntity

@Dao
interface UserDao {
//    @Query("SELECT * FROM UserEntity ")
//    fun getItems(): List<User>
//
//
//    @Query("DELETE FROM RoomItem")
//    fun deleteAllItems()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: UserEntity)

    @Query("SELECT * FROM UserEntity WHERE id = :id")
    fun getById(id: Long): UserEntity?
}