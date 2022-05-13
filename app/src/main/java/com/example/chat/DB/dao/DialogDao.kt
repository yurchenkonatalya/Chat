package com.example.chat.DB.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.chat.DB.entity.DialogEntity

@Dao
interface DialogDao {
    @Query("SELECT * FROM DialogEntity ORDER BY id DESC")
    fun pagingSource(): PagingSource<Int, DialogEntity>

    @Query("DELETE FROM DialogEntity WHERE id >=0")
    fun deleteAllItems()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: DialogEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(items: List<DialogEntity>)

    @Query("SELECT * FROM DialogEntity WHERE id = :id")
    fun getById(id: Long): DialogEntity?

    @Query("SELECT * FROM DialogEntity WHERE opponent_id = :id")
    fun getByOpponentId(id: Long): DialogEntity?

    @Query("UPDATE DialogEntity SET id=:id, date = :date, text = :text, info=:info, unread_msg_count= :unread_msg_count WHERE opponent_id = :opponent_id")
    fun update(
        opponent_id: Long,
        id: Long,
        date: String,
        text: String,
        info: String?,
        unread_msg_count: Int
    )

    @Query("UPDATE DialogEntity SET opponent_last_active = :last_active WHERE opponent_id = :opponent_id")
    fun updateOnline(opponent_id: Long, last_active: String)

    @Query("SELECT MAX(id) FROM DialogEntity")
    fun getMax(): Long?
}