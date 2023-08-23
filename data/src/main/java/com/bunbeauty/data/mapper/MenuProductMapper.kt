package com.bunbeauty.data.mapper

import com.bunbeauty.data.model.entity.menuproduct.MenuProductEntity
import com.bunbeauty.data.model.entity.menuproduct.MenuProductWithCategoriesEntity
import com.bunbeauty.data.model.server.MenuProductServer
import com.bunbeauty.domain.model.menuproduct.MenuProduct
import javax.inject.Inject

class MenuProductMapper @Inject constructor(private val categoryMapper: CategoryMapper) {

    fun toEntity(menuProductServer: MenuProductServer): MenuProductEntity {
        return with(menuProductServer) {
            MenuProductEntity(
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
    }

    fun toEntity(menuProduct: MenuProduct): MenuProductEntity {
        return with(menuProduct) {
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
                barcode = barcode ?: 0,
                isVisible = isVisible,
            )
        }
    }

    fun toServer(menuProduct: MenuProduct): MenuProductServer {
        return with(menuProduct) {
            MenuProductServer(
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
    }
}
