package com.payback.imagesearch.domain.usecase

import com.payback.imagesearch.domain.entity.ImagePhotos
import com.payback.imagesearch.ui.imageDetails.ImageDetailsItem
import com.payback.imagesearch.util.network.Resource

interface LoadImagesUseCase {
    suspend fun loadImages(query: String): Resource<ImagePhotos>

    suspend fun loadImageData(id: Int): Resource<ImageDetailsItem>
}