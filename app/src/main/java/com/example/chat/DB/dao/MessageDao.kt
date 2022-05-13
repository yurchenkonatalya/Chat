package com.example.chat.DB.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.chat.DB.entity.MessageEntity

@Dao
interface MessageDao {
    @Query("SELECT * FROM MessageEntity ORDER BY id DESC")
    fun pagingSource(): PagingSource<Int, MessageEntity>

    @Query("DELETE FROM MessageEntity WHERE id >=0")
    fun deleteAllItems()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: MessageEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(items: List<MessageEntity>)

    @Query("SELECT * FROM MessageEntity WHERE id = :id")
    fun getById(id: Long): MessageEntity?

    @Query("SELECT MAX(id) FROM MessageEntity")
    fun getMax(): Long?
}