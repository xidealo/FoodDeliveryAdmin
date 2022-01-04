package com.bunbeauty.data.model.entity.menu_product

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bunbeauty.data.model.server.CategoryServer

@Entity
data class MenuProductEntity(
    @PrimaryKey
    val uuid: String,
    val name: String,
    val newPrice: Int,
    val oldPrice: Int,
    val utils: String,
    val nutrition: Int,
    val description: String,
    val comboDescription: String?,
    val photoLink: String,
    val barcode: Int,
    val isVisible: Boolean,
)