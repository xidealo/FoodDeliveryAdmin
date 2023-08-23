package com.bunbeauty.data.model.entity.menuproduct

import androidx.room.Entity

@Entity(
    primaryKeys = ["menuProductUuid", "categoryUuid"],
)
data class MenuProductCategoryEntity(
    val menuProductUuid: String,
    val categoryUuid: String
)