package com.bunbeauty.data.model.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.bunbeauty.data.model.entity.menu_product.MenuProductEntity

@Entity
data class CategoryEntity(
    @PrimaryKey
    val uuid: String,
    val name: String,
    val priority: Int,
)