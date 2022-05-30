package com.payback.imagesearch.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ImageRecordDao {
    @Query("SELECT * FROM imageRecord WHERE tags LIKE :tags")
    suspend fun findRecordsByTags(tags: String): List<ImageRecord>

    @Query("SELECT * FROM imageRecord WHERE id = :first LIMIT 1")
    suspend fun findById(first: Long): ImageRecord

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAll(records: List<ImageRecord>)
}