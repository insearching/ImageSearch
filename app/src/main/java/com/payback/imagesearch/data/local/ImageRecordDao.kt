package com.payback.imagesearch.data.local

import androidx.room.*

@Dao
interface ImageRecordDao {
    @Query("SELECT * FROM imageRecord")
    suspend fun getAll(): List<ImageRecord>

    @Query("SELECT * FROM imageRecord WHERE id = :first LIMIT 1")
    suspend fun findById(first: Int): ImageRecord

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(records: List<ImageRecord>)

    @Delete
    suspend fun delete(user: ImageRecord)
}