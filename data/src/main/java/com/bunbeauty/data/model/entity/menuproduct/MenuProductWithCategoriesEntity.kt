package com.bunbeauty.data.model.entity.menuproduct

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.bunbeauty.data.model.entity.CategoryEntity

data class MenuProductWithCategoriesEntity(
    @Embedded
    val menuProductEntity: MenuProductEntity,

    @Relation(
        parentColumn = "uuid",
        entity = CategoryEntity::class,
        entityColumn = "uuid",
        associateBy = Junction(
            value = MenuProductCategoryEntity::class,
            parentColumn = "menuProductUuid",
            entityColumn = "categoryUuid"
        )
    )
    val categories: List<CategoryEntity>
)
