package com.payback.imagesearch.domain.usecase

import com.payback.imagesearch.domain.entity.Hit
import com.payback.imagesearch.ui.imageDetails.ImageDetailsItem
import com.payback.imagesearch.util.network.Resource

interface LoadImagesUseCase {
    suspend fun loadImages(query: String): Resource<List<Hit>>

    suspend fun loadImageDetails(id: Long): Resource<ImageDetailsItem>
}