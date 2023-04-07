package com.bunbeauty.data.mapper

import com.bunbeauty.data.model.entity.menu_product.MenuProductEntity
import com.bunbeauty.data.model.entity.menu_product.MenuProductWithCategoriesEntity
import com.bunbeauty.data.model.server.MenuProductServer
import com.bunbeauty.domain.model.menu_product.MenuProduct
import javax.inject.Inject

class MenuProductMapper @Inject constructor(
    private val categoryMapper: CategoryMapper
) {
    fun toEntity(menuProductServer: MenuProductServer): MenuProductEntity {
        return MenuProductEntity(
            uuid = menuProductServer.uuid ?: "",
            name = menuProductServer.name ?: "",
            newPrice = menuProductServer.newPrice ?: 0,
            oldPrice = menuProductServer.oldPrice,
            utils = menuProductServer.utils ?: "",
            nutrition = menuProductServer.nutrition ?: 0,
            description = menuProductServer.description ?: "",
            comboDescription = menuProductServer.comboDescription,
            photoLink = menuProductServer.photoLink ?: "",
            barcode = menuProductServer.barcode ?: 0,
            isVisible = menuProductServer.isVisible ?: false,
        )
    }

    fun toModel(menuProductWithCategoriesEntity: MenuProductWithCategoriesEntity): MenuProduct {
        return MenuProduct(
            uuid = menuProductWithCategoriesEntity.menuProductEntity.uuid,
            name = menuProductWithCategoriesEntity.menuProductEntity.name,
            newPrice = menuProductWithCategoriesEntity.menuProductEntity.newPrice,
            oldPrice = menuProductWithCategoriesEntity.menuProductEntity.oldPrice,
            utils = menuProductWithCategoriesEntity.menuProductEntity.utils,
            nutrition = menuProductWithCategoriesEntity.menuProductEntity.nutrition,
            description = menuProductWithCategoriesEntity.menuProductEntity.description,
            comboDescription = menuProductWithCategoriesEntity.menuProductEntity.comboDescription,
            photoLink = menuProductWithCategoriesEntity.menuProductEntity.photoLink,
            barcode = menuProductWithCategoriesEntity.menuProductEntity.barcode,
            isVisible = menuProductWithCategoriesEntity.menuProductEntity.isVisible,
            categories = menuProductWithCategoriesEntity.categories.map(categoryMapper::toModel)
        )
    }

    fun toModel(menuProductServer: MenuProductServer): MenuProduct {
        return MenuProduct(
            uuid = menuProductServer.uuid ?: "",
            name = menuProductServer.name ?: "",
            newPrice = menuProductServer.newPrice ?: 0,
            oldPrice = menuProductServer.oldPrice,
            utils = menuProductServer.utils ?: "",
            nutrition = menuProductServer.nutrition ?: 0,
            description = menuProductServer.description ?: "",
            comboDescription = menuProductServer.comboDescription,
            photoLink = menuProductServer.photoLink ?: "",
            barcode = menuProductServer.barcode ?: 0,
            isVisible = menuProductServer.isVisible ?: false,
            categories = menuProductServer.categories?.map(categoryMapper::toModel) ?: emptyList()
        )
    }
}