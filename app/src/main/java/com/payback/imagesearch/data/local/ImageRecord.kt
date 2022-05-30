package com.payback.imagesearch.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity
data class ImageRecord(
    @PrimaryKey
    val id: Long,
    @ColumnInfo(name = "collections")
    val collections: Int,
    @ColumnInfo(name = "comments")
    val comments: Int,
    @ColumnInfo(name = "downloads")
    val downloads: Int,
    @ColumnInfo(name = "imageHeight")
    val imageHeight: Int,
    @ColumnInfo(name = "imageSize")
    val imageSize: Int,
    @ColumnInfo(name = "imageWidth")
    val imageWidth: Int,
    @ColumnInfo(name = "largeImageURL")
    val largeImageURL: String,
    @ColumnInfo(name = "likes")
    val likes: Int,
    @ColumnInfo(name = "pageURL")
    val pageURL: String,
    @ColumnInfo(name = "previewHeight")
    val previewHeight: Int,
    @ColumnInfo(name = "previewURL")
    val previewURL: String,
    @ColumnInfo(name = "previewWidth")
    val previewWidth: Int,
    @ColumnInfo(name = "tags")
    val tags: String,
    @ColumnInfo(name = "type")
    val type: String,
    @ColumnInfo(name = "user")
    val user: String,
    @ColumnInfo(name = "userImageURL")
    val userImageURL: String,
    @ColumnInfo(name = "userId")
    val userId: Int,
    @ColumnInfo(name = "views")
    val views: Int,
    @ColumnInfo(name = "webformatHeight")
    val webformatHeight: Int,
    @ColumnInfo(name = "webformatURL")
    val webformatURL: String,
    @ColumnInfo(name = "webformatWidth")
    val webformatWidth: Int
)