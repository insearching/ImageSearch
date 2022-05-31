package com.payback.imagesearch.ui.imageDetails

data class ImageDetailsItem(
    val largeImageLink: String,
    val userName: String,
    val tags: String,
    val likes: Int,
    val downloads: Int,
    val comments: Int,
)
