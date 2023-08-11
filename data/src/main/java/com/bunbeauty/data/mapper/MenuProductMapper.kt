package com.bunbeauty.data.mapper

import com.bunbeauty.data.model.entity.menu_product.MenuProductEntity
import com.bunbeauty.data.model.entity.menu_product.MenuProductWithCategoriesEntity
import com.bunbeauty.data.model.server.MenuProductServer
import com.bunbeauty.domain.model.menu_product.MenuProduct

fun MenuProductServer.toEntity(): MenuProductEntity {
    return MenuProductEntity(
        uuid = uuid ?: "",
        name = name ?: "",
        newPrice = newPrice ?: 0,
        oldPrice = oldPrice,
        utils = utils ?: "",
        nutrition = nutrition ?: 0,
        description = description ?: "",
        comboDescription = comboDescription,
        photoLink = photoLink ?: "",
        barcode = barcode ?: 0,
        isVisible = isVisible ?: false,
    )
}

fun MenuProductWithCategoriesEntity.toModel(categoryMapper: CategoryMapper): MenuProduct {
    return MenuProduct(
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

fun MenuProductServer.toModel(categoryMapper: CategoryMapper): MenuProduct {
    return MenuProduct(
        uuid = uuid ?: "",
        name = name ?: "",
        newPrice = newPrice ?: 0,
        oldPrice = oldPrice,
        utils = utils ?: "",
        nutrition = nutrition ?: 0,
        description = description ?: "",
        comboDescription = comboDescription,
        photoLink = photoLink ?: "",
        barcode = barcode ?: 0,
        isVisible = isVisible ?: false,
        categories = categories?.map(categoryMapper::toModel) ?: emptyList()
    )
}

fun MenuProduct.toEntity(): MenuProductEntity {
    return MenuProductEntity(
        uuid = uuid,
        name = name,
        newPrice = newPrice,
        oldPrice = oldPrice,
        utils = utils ?: "",
        nutrition = nutrition ?: 0,
        description = description,
        comboDescription = comboDescription,
        photoLink = photoLink,
        barcode = barcode ?: 0,
        isVisible = isVisible,
    )
}

fun MenuProduct.toServer(): MenuProductServer {
    return MenuProductServer(
        uuid = uuid,
        name = name,
        newPrice = newPrice,
        oldPrice = oldPrice,
        utils = utils,
        nutrition = nutrition ?: 0,
        description = description,
        comboDescription = comboDescription,
        photoLink = photoLink,
        barcode = barcode ?: 0,
        isVisible = isVisible,
    )
}