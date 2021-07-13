package com.bunbeauty.presentation

import android.os.Parcelable
import com.bunbeauty.presentation.list.ListModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class ListData(
    val title: String?,
    val list: List<ListModel>,
    val requestKey: String,
    val selectedKey: String,
): Parcelable