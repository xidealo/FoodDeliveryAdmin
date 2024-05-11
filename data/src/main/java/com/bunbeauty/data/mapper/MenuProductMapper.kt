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

class MenuProductMapper @Inject constructor(private val categoryMapper: CategoryMapper) {

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
                isVisible = isVisible
            )
        }
    }

    fun toModel(menuProductWithCategoriesEntity: MenuProductWithCategoriesEntity): MenuProduct {
        return with(menuProductWithCategoriesEntity) {
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
                categories = categories.map(categoryMapper::toModel)
            )
        }
    }

    fun toModel(menuProductServer: MenuProductServer): MenuProduct {
        return with(menuProductServer) {
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
                categories = categories.map(categoryMapper::toModel)
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
                categoryUuids = categoryUuids
            )
        }
    }
}

val mapMenuProductPostToMenuProductPostServer: MenuProductPost.() -> MenuProductPostServer = {
    MenuProductPostServer(
        isVisible = isVisible,
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
        isRecommended = isRecommended
    )
}
