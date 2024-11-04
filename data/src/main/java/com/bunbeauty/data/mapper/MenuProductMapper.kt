package com.bunbeauty.data.mapper

import com.bunbeauty.data.model.server.menuproduct.MenuProductPatchServer
import com.bunbeauty.data.model.server.menuproduct.MenuProductPostServer
import com.bunbeauty.data.model.server.menuproduct.MenuProductServer
import com.bunbeauty.domain.model.menuproduct.MenuProduct
import com.bunbeauty.domain.model.menuproduct.MenuProductPost
import com.bunbeauty.domain.model.menuproduct.UpdateMenuProduct
import javax.inject.Inject

class MenuProductMapper @Inject constructor() {

    fun toModel(menuProductServer: MenuProductServer): MenuProduct {
        return menuProductServer.run {
            MenuProduct(
                uuid = uuid,
                name = name,
                newPrice = newPrice,
                oldPrice = oldPrice,
                units = utils ?: "",
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
