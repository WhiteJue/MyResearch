package com.my.search.model

import android.os.Parcel
import android.os.Parcelable

data class SearchItem(val title: String, val imageUrl: String, val type: Int, val targetUrl: String) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: ""
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(imageUrl)
        parcel.writeInt(type)
        parcel.writeString(targetUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SearchItem> {
        override fun createFromParcel(parcel: Parcel): SearchItem {
            return SearchItem(parcel)
        }

        override fun newArray(size: Int): Array<SearchItem?> {
            return arrayOfNulls(size)
        }
    }
}

class SearchItemResponse() : ArrayList<SearchItem>()