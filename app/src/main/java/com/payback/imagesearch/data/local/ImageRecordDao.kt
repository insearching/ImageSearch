package com.payback.imagesearch.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ImageRecordDao {
    @Query("SELECT * FROM imageRecord WHERE tags LIKE :tag")
    suspend fun findRecordsByTag(tag: String): List<ImageRecord>?

    @Query("SELECT * FROM imageRecord WHERE id = :id LIMIT 1")
    suspend fun findById(id: Long): ImageRecord?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAll(records: List<ImageRecord>)
}