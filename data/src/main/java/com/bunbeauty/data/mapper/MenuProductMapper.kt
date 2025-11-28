package com.bunbeauty.data.mapper

import com.bunbeauty.data.model.server.menuproduct.MenuProductPatchServer
import com.bunbeauty.data.model.server.menuproduct.MenuProductPostServer
import com.bunbeauty.data.model.server.menuproduct.MenuProductServer
import com.bunbeauty.domain.model.addition.Addition
import com.bunbeauty.domain.model.additiongroup.AdditionGroup
import com.bunbeauty.domain.model.additiongroup.AdditionGroupWithAdditions
import com.bunbeauty.domain.model.menuproduct.MenuProduct
import com.bunbeauty.domain.model.menuproduct.MenuProductPost
import com.bunbeauty.domain.model.menuproduct.UpdateMenuProduct

class MenuProductMapper {

    fun toModel(menuProductServer: MenuProductServer): MenuProduct {
        return menuProductServer.run {
            MenuProduct(
                uuid = uuid,
                name = name,
                newPrice = newPrice,
                oldPrice = oldPrice,
                units = utils.orEmpty(),
                nutrition = nutrition ?: 0,
                description = description,
                comboDescription = comboDescription,
                photoLink = photoLink,
                barcode = barcode,
                isVisible = isVisible,
                isRecommended = isRecommended,
                categoryUuids = categories.map { category ->
                    category.uuid
                },
                additionGroups = menuProductServer.additionGroupServers.map { additionGroupServer ->
                    AdditionGroupWithAdditions(
                        additionList = additionGroupServer.additionServerList.map { additionServer ->
                            Addition(
                                isVisible = additionServer.isVisible,
                                name = additionServer.name,
                                photoLink = additionServer.photoLink,
                                price = additionServer.price,
                                uuid = additionServer.uuid,
                                fullName = additionServer.fullName,
                                priority = additionServer.priority,
                                tag = additionServer.tag
                            )
                        },
                        additionGroup = AdditionGroup(
                            isVisible = additionGroupServer.isVisible,
                            name = additionGroupServer.name,
                            singleChoice = additionGroupServer.singleChoice,
                            uuid = additionGroupServer.uuid,
                            priority = additionGroupServer.priority
                        )
                    )
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
