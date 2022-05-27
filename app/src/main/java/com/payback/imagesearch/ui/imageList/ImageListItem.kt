package com.payback.imagesearch.ui.imageList

import android.os.Parcel
import android.os.Parcelable

data class ImageListItem(
    val id: Int,
    val thumbnail: String,
    val userName: String,
    val tags: String,
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readInt(),
        source.readString() ?: throw Exception("Thumbnail can not be null"),
        source.readString() ?: throw Exception("Username can not be null"),
        source.readString() ?: throw Exception("Tags can not be null"),
    )

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeInt(id)
        dest?.writeString(thumbnail)
        dest?.writeString(userName)
        dest?.writeString(tags)
    }

    companion object CREATOR : Parcelable.Creator<ImageListItem> {
        override fun createFromParcel(parcel: Parcel): ImageListItem {
            return ImageListItem(parcel)
        }

        override fun newArray(size: Int): Array<ImageListItem?> {
            return arrayOfNulls(size)
        }
    }
}
