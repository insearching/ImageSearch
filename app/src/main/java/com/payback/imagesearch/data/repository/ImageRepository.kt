package com.payback.imagesearch.data.repository

import com.payback.imagesearch.domain.entity.Hit
import com.payback.imagesearch.domain.entity.ImagePhotos
import com.payback.imagesearch.util.network.Resource

interface ImageRepository {
    suspend fun getImages(searchQuery: String): Resource<ImagePhotos>

    suspend fun getImageRecordById(id: Int): Resource<Hit>
}