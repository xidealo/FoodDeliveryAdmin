package com.bunbeauty.domain.model.menu_product

import android.os.Parcelable
import com.bunbeauty.domain.model.Category
import kotlinx.parcelize.Parcelize

@Parcelize
data class MenuProduct(
    val uuid: String,
    val name: String,
    val newPrice: Int,
    val oldPrice: Int?,
    val utils: String,
    val nutrition: Int?,
    val description: String,
    val comboDescription: String?,
    val photoLink: String,
    val barcode: Int?,
    val isVisible: Boolean,
    val categories: List<Category>,
) : Parcelable