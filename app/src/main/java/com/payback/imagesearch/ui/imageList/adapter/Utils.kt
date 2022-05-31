package com.payback.imagesearch.ui.imageList.adapter

import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.databinding.BindingAdapter
import coil.load
import coil.request.CachePolicy
import coil.transform.CircleCropTransformation
import com.payback.imagesearch.R


@BindingAdapter("thumbnailUrl")
fun ImageView.loadImageThumbnail(url: String?) {
    load(url) {
        crossfade(true)
        placeholder(R.drawable.ic_placeholder)
        error(R.drawable.ic_failure)
        fallback(R.drawable.ic_failure)
        transformations(CircleCropTransformation())
        memoryCachePolicy(CachePolicy.DISABLED)
    }
}


@BindingAdapter("imageUrl")
fun ImageView.loadImage(url: String?) {
    load(url) {
        crossfade(true)
        placeholder(R.drawable.ic_placeholder)
        error(R.drawable.ic_failure)
        fallback(R.drawable.ic_failure)
    }
}

@BindingAdapter("searchClickable")
fun SearchView.searchClickable(isClickable: Boolean) {
    setOnClickListener { isIconified = !isClickable }
}