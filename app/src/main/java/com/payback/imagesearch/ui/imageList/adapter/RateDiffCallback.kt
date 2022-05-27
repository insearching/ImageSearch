package com.payback.imagesearch.ui.imageList.adapter

import android.os.Bundle
import androidx.annotation.Nullable
import androidx.recyclerview.widget.DiffUtil
import com.payback.imagesearch.ui.imageList.ImageListItem

class RateDiffCallback(
    private val oldItems: List<ImageListItem>,
    private val newItems: List<ImageListItem>,
) :
    DiffUtil.Callback() {

    companion object {
        const val THUMBNAIL_CHANGE = "THUMBNAIL_CHANGE"
    }

    override fun getOldListSize(): Int {
        return oldItems.size
    }

    override fun getNewListSize(): Int {
        return newItems.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems[oldItemPosition].id == newItems[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        if (oldItemPosition == 0 && newItemPosition == 0) return true
        return oldItems[oldItemPosition] == newItems[newItemPosition]
    }

    @Nullable
    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        if (oldItemPosition == 0) return null
        if (oldItems.size <= oldItemPosition || newItems.size <= newItemPosition) return null
        val oldItem = oldItems[oldItemPosition]
        val newItem = newItems[newItemPosition]

        val payload = Bundle()
        if (oldItem.thumbnail != newItem.thumbnail) {
            payload.putString(THUMBNAIL_CHANGE, newItem.thumbnail)
        }

        return if (payload.isEmpty) null else payload
    }
}