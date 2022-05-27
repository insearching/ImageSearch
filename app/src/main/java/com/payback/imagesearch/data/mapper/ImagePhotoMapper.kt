package com.payback.imagesearch.data.mapper

import com.payback.imagesearch.data.api.digitalmodel.DigitalHit
import com.payback.imagesearch.data.api.digitalmodel.DigitalImagePhotos
import com.payback.imagesearch.data.local.ImageRecord
import com.payback.imagesearch.domain.entity.Hit
import com.payback.imagesearch.domain.entity.ImagePhotos

object ImagePhotoMapper {
    val Hit.toDaoModel
        get() = ImageRecord(
            collections = collections,
            comments = comments,
            downloads = downloads,
            id = id,
            imageHeight = imageHeight,
            imageSize = imageSize,
            imageWidth = imageWidth,
            largeImageURL = largeImageURL,
            likes = likes,
            pageURL = pageURL,
            previewHeight = previewHeight,
            previewURL = previewURL,
            previewWidth = previewWidth,
            tags = tags,
            type = type,
            user = user,
            userImageURL = userImageURL,
            userId = userId,
            views = views,
            webformatHeight = webformatHeight,
            webformatURL = webformatURL,
            webformatWidth = webformatWidth
        )

    val ImageRecord.toDomainModel
        get() = Hit(
            collections = collections,
            comments = comments,
            downloads = downloads,
            id = id,
            imageHeight = imageHeight,
            imageSize = imageSize,
            imageWidth = imageWidth,
            largeImageURL = largeImageURL,
            likes = likes,
            pageURL = pageURL,
            previewHeight = previewHeight,
            previewURL = previewURL,
            previewWidth = previewWidth,
            tags = tags,
            type = type,
            user = user,
            userImageURL = userImageURL,
            userId = userId,
            views = views,
            webformatHeight = webformatHeight,
            webformatURL = webformatURL,
            webformatWidth = webformatWidth
        )

    val DigitalImagePhotos.toDomainModel
        get() = ImagePhotos(
            hits = hits.asSequence().map { it.toDomainModel }.toList(),
            total = total,
            totalHits = totalHits,
        )

    private val DigitalHit.toDomainModel
        get() = Hit(
            collections = collections,
            comments = comments,
            downloads = downloads,
            id = id,
            imageHeight = imageHeight,
            imageSize = imageSize,
            imageWidth = imageWidth,
            largeImageURL = largeImageURL,
            likes = likes,
            pageURL = pageURL,
            previewHeight = previewHeight,
            previewURL = previewURL,
            previewWidth = previewWidth,
            tags = tags,
            type = type,
            user = user,
            userImageURL = userImageURL,
            userId = user_id,
            views = views,
            webformatHeight = webformatHeight,
            webformatURL = webformatURL,
            webformatWidth = webformatWidth
        )
}