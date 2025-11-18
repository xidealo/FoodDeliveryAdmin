package com.bunbeauty.data.mapper.category

import com.bunbeauty.data.model.server.category.CategoryPatchServer
import com.bunbeauty.data.model.server.category.CategoryServer
import com.bunbeauty.domain.feature.menu.common.model.Category
import com.bunbeauty.domain.feature.menu.common.model.UpdateCategory

class CategoryMapper {
    fun toModel(categoryServer: CategoryServer): Category =
        Category(
            uuid = categoryServer.uuid,
            name = categoryServer.name,
            priority = categoryServer.priority,
        )

    fun categoryServer(categoryServer: CategoryServer): Category =
        Category(
            uuid = categoryServer.uuid,
            name = categoryServer.name,
            priority = categoryServer.priority,
        )

    fun toPatchServer(updateCategory: UpdateCategory): CategoryPatchServer =
        CategoryPatchServer(
            name = updateCategory.name,
            priority = updateCategory.priority,
        )

    fun categoryMapper(category: Category): CategoryServer =
        CategoryServer(
            uuid = category.uuid,
            name = category.name,
            priority = category.priority,
        )
}
