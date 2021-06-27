package com.bunbeauty.data.mapper.cart_product

import com.bunbeauty.data.mapper.menu_product.IServerMenuProductMapper
import com.bunbeauty.domain.model.cart_product.CartProduct
import com.bunbeauty.domain.model.cart_product.ServerCartProduct
import javax.inject.Inject

class CartProductMapper @Inject constructor(
    private val menuProductMapper: IServerMenuProductMapper
): ICartProductMapper {

    override fun from(model: ServerCartProduct): CartProduct {
        return CartProduct(
            count = model.count,
            menuProduct = menuProductMapper.from(model.menuProduct)
        )
    }

    override fun to(model: CartProduct): ServerCartProduct {
        return ServerCartProduct(
            count = model.count,
            menuProduct = menuProductMapper.to(model.menuProduct)
        )
    }
}