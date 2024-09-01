package com.bunbeauty.data.model.entity.menuproduct

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MenuProductEntity(
    @PrimaryKey
    val uuid: String,
    val name: String,
    val newPrice: Int,
    val oldPrice: Int?,
    val utils: String?,
    val nutrition: Int?,
    val description: String,
    val comboDescription: String?,
    val photoLink: String,
    val barcode: Int,
    val isRecommended: Boolean,
    val isVisible: Boolean,
    val categoryUuids: String,
)
