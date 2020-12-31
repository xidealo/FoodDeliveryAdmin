package com.bunbeauty.fooddeliveryadmin.data.local.storage

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CommonDataStoreModel(
    var token: String = "",
): Parcelable {
}