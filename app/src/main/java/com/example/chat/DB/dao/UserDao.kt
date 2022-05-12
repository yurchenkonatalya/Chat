package com.example.chat.DB.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.chat.DB.entity.UserEntity

@Dao
interface UserDao {
    @Query("SELECT * FROM UserEntity WHERE isAuthorizeInfo = 0 ORDER BY responseIndex")
    fun pagingSource(): PagingSource<Int, UserEntity>

    @Query("DELETE FROM UserEntity WHERE isAuthorizeInfo = :isAuthorizeInfo")
    fun deleteAllItems(isAuthorizeInfo: Boolean)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: UserEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(items: List<UserEntity>)

    @Query("SELECT * FROM UserEntity WHERE id = :id AND isAuthorizeInfo = :isAuthorizeInfo")
    fun getById(id: Long, isAuthorizeInfo: Boolean): UserEntity?

    @Query("SELECT * FROM UserEntity")
    fun get(): List<UserEntity>
}
