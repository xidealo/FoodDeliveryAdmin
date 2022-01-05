package com.bunbeauty.data.mapper.menu_product

import com.bunbeauty.data.model.entity.MenuProductEntity
import com.bunbeauty.domain.model.menu_product.MenuProduct

interface IMenuProductMapper {
    //fun toEntityModel(serverMenuProduct: ServerMenuProduct): MenuProductEntity
    fun toModel(menuProductEntity: MenuProductEntity): MenuProduct
    //fun toModel(serverMenuProduct: ServerMenuProduct): MenuProduct
    //fun toServerModel(menuProduct: MenuProduct): ServerMenuProduct
}