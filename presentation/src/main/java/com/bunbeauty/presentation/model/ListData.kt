package com.bunbeauty.presentation.model

import android.os.Parcelable
import com.bunbeauty.presentation.list.ListItemModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class ListData(
    val title: String?,
    val listItem: List<ListItemModel>,
    val requestKey: String,
    val selectedKey: String,
): Parcelable