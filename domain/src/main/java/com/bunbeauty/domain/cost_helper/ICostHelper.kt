package com.bunbeauty.domain.cost_helper

import com.bunbeauty.data.model.CartProduct

interface ICostHelper {
    fun getCost(cartProduct: CartProduct): Int
}