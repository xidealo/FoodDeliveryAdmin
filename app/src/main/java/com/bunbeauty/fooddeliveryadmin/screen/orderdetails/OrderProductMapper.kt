package com.bunbeauty.fooddeliveryadmin.screen.orderdetails

import com.bunbeauty.common.Constants.BULLET_SYMBOL
import com.bunbeauty.common.Constants.RUBLE_CURRENCY
import com.bunbeauty.common.Constants.X_SYMBOL
import com.bunbeauty.domain.model.cartproduct.OrderProduct
import javax.inject.Inject

class OrderProductMapper @Inject constructor() {

    fun map(orderProduct: OrderProduct): OrderDetailsUiState.Product {
        return OrderDetailsUiState.Product(
            title = if (orderProduct.comboDescription == null) {
                orderProduct.name
            } else {
                orderProduct.name + "\n" + (orderProduct.comboDescription ?: "")
            },
            price = if (orderProduct.additionsPrice == null) {
                "${orderProduct.newPrice} $RUBLE_CURRENCY"
            } else {
                "(${orderProduct.newPrice} $RUBLE_CURRENCY + ${orderProduct.additionsPrice} $RUBLE_CURRENCY)"
            },
            count = "$X_SYMBOL ${orderProduct.count}",
            cost = "${orderProduct.newTotalCost} $RUBLE_CURRENCY",
            additions = orderProduct.additions.takeIf { additions ->
                additions.isNotEmpty()
            }?.let { additions ->
                additions.joinToString(" $BULLET_SYMBOL ") { orderAddition ->
                    orderAddition.name
                }
            }
        )
    }
}
