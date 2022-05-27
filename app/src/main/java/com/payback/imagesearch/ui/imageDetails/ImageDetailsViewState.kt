package com.payback.imagesearch.ui.imageDetails

import android.view.View
import com.payback.imagesearch.domain.entity.ImagePhotos
import com.payback.imagesearch.ui.imageList.ImageListItem

data class ImageDetailsViewState(
    val isLoading: Boolean = false,
    val error: Error? = null,
    val item: ImageDetailsItem?,
) {
    sealed class Error(val message: String) {
        data class BaseError(val _message: String) : Error(_message)
    }

    val progressVisible = if (isLoading) View.VISIBLE else View.GONE

    val errorVisible = if (error != null || item == null) View.VISIBLE else View.GONE

    val dataVisible = if (!isLoading) View.VISIBLE else View.GONE
}
