package com.payback.imagesearch.ui.imageList

import android.view.View
import com.payback.imagesearch.domain.entity.ImagePhotos

data class ImageListViewState(
    val isLoading: Boolean = false,
    val error: Error? = null,
    val imagePhotos: ImagePhotos?,
) {
    sealed class Error(val message: String) {
        data class BaseError(val _message: String) : Error(_message)
    }

    val progressVisible = if (isLoading) View.VISIBLE else View.GONE

    val dataVisible = if (error == null) View.VISIBLE else View.GONE

    val errorVisible = if (error != null || imagePhotos?.hits?.isEmpty() == true) View.VISIBLE else View.GONE

    val errorText = error?.message ?: if(imagePhotos?.hits?.isEmpty() == true) "No images found" else ""
}
