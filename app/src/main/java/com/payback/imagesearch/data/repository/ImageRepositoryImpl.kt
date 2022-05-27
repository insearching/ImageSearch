package com.payback.imagesearch.data.repository

import com.payback.imagesearch.data.api.digitalcontract.DigitalApiPixabay
import com.payback.imagesearch.data.local.ImageRecordDao
import com.payback.imagesearch.data.mapper.ImagePhotoMapper.toDaoModel
import com.payback.imagesearch.data.mapper.ImagePhotoMapper.toDomainModel
import com.payback.imagesearch.domain.entity.Hit
import com.payback.imagesearch.domain.entity.ImagePhotos
import com.payback.imagesearch.util.network.Resource
import com.payback.imagesearch.util.network.toResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URLEncoder.encode
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageRepositoryImpl @Inject constructor(
    private val apiAccessor: DigitalApiPixabay,
    private val daoAccessor: ImageRecordDao,
) : ImageRepository {
    override suspend fun getImages(searchQuery: String): Resource<ImagePhotos> =
        withContext(Dispatchers.IO) {
            try {

                val digitalImagePhotos =
                    apiAccessor.getImages(searchQuery = encode(searchQuery, "utf-8"))
                        .toResource().data
                        ?: return@withContext Resource.error("Failed to load images")

                val imageData = digitalImagePhotos.toDomainModel
                daoAccessor.insertAll(imageData.hits.asSequence().map { it.toDaoModel }.toList())
                return@withContext Resource.success(imageData)
            } catch (e: Exception) {
                return@withContext Resource.error("No internet connection")
            }
        }

    override suspend fun getImageRecordById(id: Int): Resource<Hit> =
        withContext(Dispatchers.IO) {
            return@withContext Resource.success(daoAccessor.findById(id).toDomainModel)
        }
}