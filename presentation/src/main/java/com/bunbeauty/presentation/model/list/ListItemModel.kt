package com.bunbeauty.presentation.model.list

import android.os.Parcelable

abstract class ListItemModel: Parcelable {
    abstract val title: String
}