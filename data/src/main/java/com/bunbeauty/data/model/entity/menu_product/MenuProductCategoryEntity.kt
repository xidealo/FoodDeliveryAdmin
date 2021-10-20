package com.bunbeauty.data.model.entity.menu_product

import androidx.room.Entity

@Entity(
    primaryKeys = ["menuProductUuid", "categoryUuid"],
)
data class MenuProductCategoryEntity(
    val menuProductUuid: String,
    val categoryUuid: String
)