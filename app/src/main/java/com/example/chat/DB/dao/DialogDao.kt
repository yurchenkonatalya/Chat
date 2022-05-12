package com.example.chat.DB.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.chat.DB.entity.DialogEntity

@Dao
interface DialogDao {
    @Query("SELECT * FROM DialogEntity ORDER BY responseIndex")
    fun pagingSource(): PagingSource<Int, DialogEntity>

    @Query("DELETE FROM DialogEntity WHERE id >=0")
    fun deleteAllItems()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: DialogEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(items: List<DialogEntity>)

    @Query("SELECT * FROM DialogEntity WHERE id = :id")
    fun getById(id: Long): DialogEntity?
}
