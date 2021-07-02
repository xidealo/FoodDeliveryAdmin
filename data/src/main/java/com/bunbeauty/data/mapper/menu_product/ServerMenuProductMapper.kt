package com.bunbeauty.data.mapper.menu_product

import com.bunbeauty.domain.enums.ProductCode
import com.bunbeauty.domain.model.menu_product.MenuProduct
import com.bunbeauty.domain.model.ServerMenuProduct
import javax.inject.Inject

class ServerMenuProductMapper @Inject constructor() : IServerMenuProductMapper {

    override fun from(model: ServerMenuProduct): MenuProduct {
        return MenuProduct(
            uuid = model.uuid,
            name = model.name,
            cost = model.cost,
            discountCost = model.discountCost,
            weight = model.weight,
            description = model.description,
            comboDescription = model.comboDescription,
            photoLink = model.photoLink,
            onFire = model.onFire,
            inOven = model.inOven,
            productCode = ProductCode.valueOf(model.productCode),
            barcode = model.barcode,
            visible = model.visible
        )
    }

    override fun to(model: MenuProduct): ServerMenuProduct {
        return ServerMenuProduct(
            uuid = model.uuid,
            name = model.name,
            cost = model.cost,
            discountCost = model.discountCost,
            weight = model.weight,
            description = model.description,
            comboDescription = model.comboDescription,
            photoLink = model.photoLink,
            onFire = model.onFire,
            inOven = model.inOven,
            productCode = model.productCode.name,
            barcode = model.barcode,
            visible = model.visible
        )
    }
}