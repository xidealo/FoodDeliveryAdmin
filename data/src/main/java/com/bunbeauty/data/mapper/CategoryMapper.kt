package com.bunbeauty.data.mapper

import com.bunbeauty.data.model.entity.CategoryEntity
import com.bunbeauty.data.model.server.category.CategoryServer
import com.bunbeauty.domain.feature.menu.common.model.Category
import javax.inject.Inject

class CategoryMapper @Inject constructor() {

    fun toModel(categoryEntity: CategoryEntity): Category {
        return Category(
            uuid = categoryEntity.uuid,
            name = categoryEntity.name,
            priority = categoryEntity.priority
        )
    }

    fun toModel(categoryServer: CategoryServer): Category {
        return Category(
            uuid = categoryServer.uuid,
            name = categoryServer.name,
            priority = categoryServer.priority
        )
    }

    fun toEntity(categoryServer: CategoryServer): CategoryEntity {
        return CategoryEntity(
            uuid = categoryServer.uuid,
            name = categoryServer.name,
            priority = categoryServer.priority
        )
    }
}
