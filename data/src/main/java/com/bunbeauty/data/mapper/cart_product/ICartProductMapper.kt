package com.bunbeauty.data.mapper.cart_product

import com.bunbeauty.data.mapper.Mapper
import com.bunbeauty.domain.model.cart_product.CartProduct
import com.bunbeauty.data.model.server.ServerCartProduct

interface ICartProductMapper: Mapper<ServerCartProduct, CartProduct>