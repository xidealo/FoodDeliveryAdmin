package com.bunbeauty.domain.model.cart_product

import android.os.Parcelable
import com.bunbeauty.domain.model.menu_product.MenuProduct
import kotlinx.parcelize.Parcelize

@Parcelize
data class CartProduct(
    var count: Int = 1,
    var menuProduct: MenuProduct,
) : Parcelable