package com.bunbeauty.data.mapper.menu_product

import com.bunbeauty.data.model.entity.MenuProductEntity
import com.bunbeauty.domain.enums.ProductCode
import com.bunbeauty.data.model.server.ServerMenuProduct
import com.bunbeauty.domain.model.menu_product.MenuProduct
import javax.inject.Inject

class MenuProductMapper @Inject constructor() : IMenuProductMapper {

    override fun toEntityModel(serverMenuProduct: ServerMenuProduct): MenuProductEntity {
        return MenuProductEntity(
            uuid = serverMenuProduct.uuid ?: "",
            name = serverMenuProduct.name,
            cost = serverMenuProduct.cost,
            discountCost = serverMenuProduct.discountCost,
            weight = serverMenuProduct.weight,
            description = serverMenuProduct.description,
            comboDescription = serverMenuProduct.comboDescription,
            photoLink = serverMenuProduct.photoLink,
            onFire = serverMenuProduct.onFire,
            inOven = serverMenuProduct.inOven,
            productCode = ProductCode.valueOf(serverMenuProduct.productCode),
            barcode = serverMenuProduct.barcode,
            visible = serverMenuProduct.visible
        )
    }

    override fun toModel(menuProductEntity: MenuProductEntity): MenuProduct {
        return MenuProduct(
            uuid = menuProductEntity.uuid,
            name = menuProductEntity.name,
            cost = menuProductEntity.cost,
            discountCost = menuProductEntity.discountCost,
            weight = menuProductEntity.weight,
            description = menuProductEntity.description,
            comboDescription = menuProductEntity.comboDescription,
            photoLink = menuProductEntity.photoLink,
            onFire = menuProductEntity.onFire,
            inOven = menuProductEntity.inOven,
            productCode = menuProductEntity.productCode,
            barcode = menuProductEntity.barcode,
            visible = menuProductEntity.visible
        )
    }

    override fun toModel(serverMenuProduct: ServerMenuProduct): MenuProduct {
        return MenuProduct(
            uuid = serverMenuProduct.uuid ?: "",
            name = serverMenuProduct.name,
            cost = serverMenuProduct.cost,
            discountCost = serverMenuProduct.discountCost,
            weight = serverMenuProduct.weight,
            description = serverMenuProduct.description,
            comboDescription = serverMenuProduct.comboDescription,
            photoLink = serverMenuProduct.photoLink,
            onFire = serverMenuProduct.onFire,
            inOven = serverMenuProduct.inOven,
            productCode = ProductCode.valueOf(serverMenuProduct.productCode),
            barcode = serverMenuProduct.barcode,
            visible = serverMenuProduct.visible
        )
    }

    override fun toServerModel(menuProduct: MenuProduct): ServerMenuProduct {
        return ServerMenuProduct(
            uuid = menuProduct.uuid,
            name = menuProduct.name,
            cost = menuProduct.cost,
            discountCost = menuProduct.discountCost,
            weight = menuProduct.weight,
            description = menuProduct.description,
            comboDescription = menuProduct.comboDescription,
            photoLink = menuProduct.photoLink,
            onFire = menuProduct.onFire,
            inOven = menuProduct.inOven,
            productCode = menuProduct.productCode.name,
            barcode = menuProduct.barcode,
            visible = menuProduct.visible
        )
    }
}