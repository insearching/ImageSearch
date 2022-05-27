package com.payback.imagesearch.ui.imageList.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.payback.imagesearch.R
import com.payback.imagesearch.databinding.ItemImageBinding
import com.payback.imagesearch.ui.imageList.ImageListItem


class ImageListAdapter(
    private val onItemSelected: (listItem: ImageListItem) -> Unit,
) : RecyclerView.Adapter<ImageListAdapter.ViewHolder>() {

    private var listItems = mutableListOf<ImageListItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = listItems.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listItems[position])
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: List<Any>) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
            return
        }

        val bundle = payloads[0] as Bundle
        val newItemValue = bundle.getString(RateDiffCallback.THUMBNAIL_CHANGE, null)
        if (newItemValue != null) {
            with(holder.itemView.findViewById<ImageView>(R.id.thumbnail_icon)) {
                loadImageThumbnail(newItemValue)
            }
        }
    }

    fun updateItems(newItems: List<ImageListItem>) {
        if (newItems.isEmpty()) return
        val diffResult = DiffUtil.calculateDiff(RateDiffCallback(listItems, newItems))
        listItems = newItems.toMutableList()
        diffResult.dispatchUpdatesTo(this)
    }

    inner class ViewHolder(
        private val viewBinding: ItemImageBinding
    ) : RecyclerView.ViewHolder(viewBinding.root) {

        fun bind(item: ImageListItem) {
            viewBinding.listItem = item
            viewBinding.root.setOnClickListener { onItemSelected(item) }
            viewBinding.executePendingBindings()
        }
    }
}