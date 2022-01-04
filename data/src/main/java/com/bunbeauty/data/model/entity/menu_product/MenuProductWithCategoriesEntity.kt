package com.bunbeauty.data.model.entity.menu_product

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.bunbeauty.data.model.entity.CategoryEntity
import com.bunbeauty.data.model.server.CategoryServer

data class MenuProductWithCategoriesEntity(
    @Embedded
    val menuProductEntity: MenuProductEntity,

    @Relation(parentColumn = "uuid", entityColumn = "menuProductUuid")
    val categories: List<CategoryEntity>,
)
