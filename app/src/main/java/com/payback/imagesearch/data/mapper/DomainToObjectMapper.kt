package com.payback.imagesearch.data.mapper

import com.payback.imagesearch.domain.entity.Hit
import com.payback.imagesearch.ui.imageDetails.ImageDetailsItem
import com.payback.imagesearch.ui.imageList.ImageListItem

object DomainToObjectMapper {
    val Hit.toListItem
        get() = ImageListItem(
            id = id,
            thumbnail = previewURL,
            userName = user,
            tags = tags,
        )

    val Hit.toItemDetails
        get() = ImageDetailsItem(
            largeImageLink = largeImageURL,
            userName = user,
            tags = tags,
            likes = likes,
            downloads = downloads,
            comments = comments,
        )
}