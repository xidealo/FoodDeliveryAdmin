package com.bunbeauty.data.model.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.bunbeauty.data.model.entity.menu_product.MenuProductEntity

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = MenuProductEntity::class,
            parentColumns = ["uuid"],
            childColumns = ["menuProductUuid"],
            onDelete = ForeignKey.CASCADE
        )],
    indices = [Index(value = ["uuid"]),
        Index(value = ["menuProductUuid"])]
)
data class CategoryEntity(
    @PrimaryKey
    val uuid: String,
    val name: String,
    val priority: Int,
    val menuProductUuid: String
)