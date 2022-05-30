package com.payback.imagesearch.ui.imageList

import android.view.View
import com.payback.imagesearch.domain.entity.Hit

data class ImageListViewState(
    val isLoading: Boolean = false,
    val error: Error? = null,
    val photos: List<Hit>?,
) {
    sealed class Error(val message: String) {
        data class BaseError(val _message: String) : Error(_message)
    }

    val progressVisible = if (isLoading) View.VISIBLE else View.GONE

    val dataVisible = if (photos?.isEmpty() == false) View.VISIBLE else View.GONE

    val errorVisible = if (photos?.isEmpty() == true) View.VISIBLE else View.GONE

    val searchClickable = true
}
