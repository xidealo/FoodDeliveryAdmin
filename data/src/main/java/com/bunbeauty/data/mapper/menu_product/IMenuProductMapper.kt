package com.bunbeauty.data.mapper.menu_product

import com.bunbeauty.data.model.entity.MenuProductEntity
import com.bunbeauty.domain.model.menu_product.MenuProduct
import com.bunbeauty.data.model.server.ServerMenuProduct

interface IMenuProductMapper {
    fun toEntityModel(serverMenuProduct: ServerMenuProduct): MenuProductEntity
    fun toModel(menuProductEntity: MenuProductEntity): MenuProduct
    fun toModel(serverMenuProduct: ServerMenuProduct): MenuProduct
    fun toServerModel(menuProduct: MenuProduct): ServerMenuProduct
}