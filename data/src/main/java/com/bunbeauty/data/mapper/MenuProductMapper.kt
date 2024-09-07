package com.bunbeauty.data.mapper

import com.bunbeauty.data.model.entity.menuproduct.MenuProductEntity
import com.bunbeauty.data.model.entity.menuproduct.MenuProductWithCategoriesEntity
import com.bunbeauty.data.model.server.menuproduct.MenuProductPatchServer
import com.bunbeauty.data.model.server.menuproduct.MenuProductPostServer
import com.bunbeauty.data.model.server.menuproduct.MenuProductServer
import com.bunbeauty.domain.model.menuproduct.MenuProduct
import com.bunbeauty.domain.model.menuproduct.MenuProductPost
import com.bunbeauty.domain.model.menuproduct.UpdateMenuProduct
import javax.inject.Inject

private const val CATEGORY_SEPARATOR = ","

class MenuProductMapper @Inject constructor() {

    fun toEntity(menuProductServer: MenuProductServer): MenuProductEntity {
        return with(menuProductServer) {
            MenuProductEntity(
                uuid = uuid,
                name = name,
                newPrice = newPrice,
                oldPrice = oldPrice,
                utils = utils ?: "",
                nutrition = nutrition ?: 0,
                description = description,
                comboDescription = comboDescription,
                photoLink = photoLink,
                barcode = barcode,
                isRecommended = isRecommended,
                isVisible = isVisible,
                categoryUuids = menuProductServer.categories.joinToString(CATEGORY_SEPARATOR) { category ->
                    category.uuid
                }
            )
        }
    }

    fun toModel(menuProductWithCategoriesEntity: MenuProductWithCategoriesEntity): MenuProduct {
        return menuProductWithCategoriesEntity.run {
            MenuProduct(
                uuid = menuProductEntity.uuid,
                name = menuProductEntity.name,
                newPrice = menuProductEntity.newPrice,
                oldPrice = menuProductEntity.oldPrice,
                utils = menuProductEntity.utils,
                nutrition = menuProductEntity.nutrition,
                description = menuProductEntity.description,
                comboDescription = menuProductEntity.comboDescription,
                photoLink = menuProductEntity.photoLink,
                barcode = menuProductEntity.barcode,
                isVisible = menuProductEntity.isVisible,
                isRecommended = menuProductEntity.isRecommended,
                categoryUuids = menuProductEntity.categoryUuids.split(CATEGORY_SEPARATOR)
            )
        }
    }

    fun toModel(menuProductServer: MenuProductServer): MenuProduct {
        return menuProductServer.run {
            MenuProduct(
                uuid = uuid,
                name = name,
                newPrice = newPrice,
                oldPrice = oldPrice,
                utils = utils ?: "",
                nutrition = nutrition ?: 0,
                description = description,
                comboDescription = comboDescription,
                photoLink = photoLink,
                barcode = barcode,
                isVisible = isVisible,
                isRecommended = isRecommended,
                categoryUuids = categories.map { category ->
                    category.uuid
                }
            )
        }
    }

    fun toPatchServer(updateMenuProduct: UpdateMenuProduct): MenuProductPatchServer {
        return with(updateMenuProduct) {
            MenuProductPatchServer(
                name = name,
                newPrice = newPrice,
                oldPrice = oldPrice,
                utils = utils,
                nutrition = nutrition,
                description = description,
                comboDescription = comboDescription,
                photoLink = photoLink,
                isVisible = isVisible,
                isRecommended = isRecommended,
                categoryUuids = categories
            )
        }
    }
}

val toMenuProductPostServer: MenuProductPost.() -> MenuProductPostServer = {
    MenuProductPostServer(
        name = name,
        photoLink = photoLink,
        newPrice = newPrice,
        oldPrice = oldPrice,
        utils = utils,
        nutrition = nutrition,
        description = description,
        comboDescription = comboDescription,
        barcode = barcode,
        categories = categories,
        isVisible = isVisible,
        isRecommended = isRecommended
    )
}
