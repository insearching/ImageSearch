package com.payback.imagesearch.domain.usecase

import com.payback.imagesearch.data.repository.ImageRepository
import com.payback.imagesearch.domain.entity.Hit
import com.payback.imagesearch.ui.imageDetails.ImageDetailsItem
import com.payback.imagesearch.data.mapper.DomainToObjectMapper.toItemDetails
import com.payback.imagesearch.util.network.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoadImagesUseCaseImpl @Inject constructor(
    private val imageRepository: ImageRepository
) : LoadImagesUseCase {
    override suspend fun loadImages(query: String): Resource<List<Hit>> =
        imageRepository.loadImages(query)

    override suspend fun loadImageData(id: Long): Resource<ImageDetailsItem> {
        val resource = imageRepository.getImageRecordById(id)
        return resource.data?.let {
            Resource.success(it.toItemDetails)
        } ?: Resource.error(message = resource.message ?: "")
    }
}