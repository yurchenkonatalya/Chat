package com.example.chat.DB.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.chat.DB.entity.LocaleEntity

@Dao
interface LocaleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: LocaleEntity)

    @Query("SELECT * FROM LocaleEntity LIMIT 1")
    fun get(): LocaleEntity?

    @Query("DELETE FROM LocaleEntity WHERE locale != -1")
    suspend fun deleteAll()
}