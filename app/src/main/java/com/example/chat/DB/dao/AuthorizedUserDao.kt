package com.example.chat.DB.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.chat.DB.entity.AuthorizedUserEntity
import com.example.chat.DB.entity.UserEntity

@Dao
interface AuthorizedUserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: AuthorizedUserEntity)

    @Query("SELECT * FROM AuthorizedUserEntity LIMIT 1")
    fun get(): AuthorizedUserEntity?

    @Query("DELETE FROM AuthorizedUserEntity WHERE id != -1")
    suspend fun deleteAll()
}
