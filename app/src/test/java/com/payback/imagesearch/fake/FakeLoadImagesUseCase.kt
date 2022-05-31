package com.payback.imagesearch.fake

import com.payback.imagesearch.domain.entity.Hit
import com.payback.imagesearch.domain.usecase.LoadImagesUseCase
import com.payback.imagesearch.ui.imageDetails.ImageDetailsItem
import com.payback.imagesearch.util.network.Resource

class FakeLoadImagesUseCase : LoadImagesUseCase {
    override suspend fun loadImages(query: String): Resource<List<Hit>> {
        return Resource.success(emptyList())
    }

    override suspend fun loadImageDetails(id: Long): Resource<ImageDetailsItem> {
        return Resource.success(ImageDetailsItem("", "", "", 0, 0, 0))
    }
}