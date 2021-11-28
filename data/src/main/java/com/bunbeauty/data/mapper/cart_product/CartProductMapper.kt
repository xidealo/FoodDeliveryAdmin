package com.bunbeauty.data.mapper.cart_product

import com.bunbeauty.data.mapper.menu_product.IMenuProductMapper
import com.bunbeauty.domain.model.cart_product.CartProduct
import com.bunbeauty.data.model.server.ServerCartProduct
import javax.inject.Inject

class CartProductMapper @Inject constructor(
    private val menuProductMapper: IMenuProductMapper
): ICartProductMapper {

    override fun from(model: ServerCartProduct): CartProduct {
        return CartProduct(
            count = model.count,
            menuProduct = menuProductMapper.toModel(model.menuProduct)
        )
    }

    override fun to(model: CartProduct): ServerCartProduct {
        return ServerCartProduct(
            count = model.count,
            menuProduct = menuProductMapper.toServerModel(model.menuProduct)
        )
    }
}