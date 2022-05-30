package com.payback.imagesearch.data.repository

import com.payback.imagesearch.data.api.digitalcontract.DigitalApiPixabay
import com.payback.imagesearch.data.local.ImageRecordDao
import com.payback.imagesearch.data.mapper.ImagePhotoMapper.toDaoModel
import com.payback.imagesearch.data.mapper.ImagePhotoMapper.toDomainModel
import com.payback.imagesearch.domain.entity.Hit
import com.payback.imagesearch.util.network.Resource
import com.payback.imagesearch.util.network.toResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageRepositoryImpl @Inject constructor(
    private val apiAccessor: DigitalApiPixabay,
    private val imageRecordsDao: ImageRecordDao,
) : ImageRepository {
    override suspend fun loadImages(searchQuery: String): Resource<List<Hit>> =
        withContext(Dispatchers.IO) {
            try {
                val digitalImagePhotos =
                    apiAccessor.loadImages(searchQuery).toResource().data
                        ?: return@withContext Resource.error("Failed to load images")

                val imageData = digitalImagePhotos.toDomainModel.hits
                imageRecordsDao.addAll(imageData.asSequence().map { it.toDaoModel }.toList())
                return@withContext Resource.success(imageData)
            } catch (e: Exception) {
                val records = searchQuery.split(" ")
                    .asSequence()
                    .map { "%$it%" }.toList()
                    .map { tag -> imageRecordsDao.findRecordsByTags(tag) }
                    .toList()
                    .flatten()
                    .distinct()
                    .asSequence().map { it.toDomainModel }.toList()

                return@withContext Resource.success(records)
            }
        }

    override suspend fun getImageRecordById(id: Long): Resource<Hit> =
        withContext(Dispatchers.IO) {
            return@withContext Resource.success(imageRecordsDao.findById(id).toDomainModel)
        }
}