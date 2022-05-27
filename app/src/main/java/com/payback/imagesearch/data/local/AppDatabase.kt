package com.payback.imagesearch.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ImageRecord::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun imageRecordDao(): ImageRecordDao
}