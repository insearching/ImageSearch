package com.payback.imagesearch.data.repository

import com.payback.imagesearch.domain.entity.Hit
import com.payback.imagesearch.util.network.Resource

interface ImageRepository {
    suspend fun loadImages(searchQuery: String): Resource<List<Hit>>

    suspend fun loadImageDetails(id: Long): Resource<Hit>
}