package com.bunbeauty.fooddeliveryadmin.screen.orderdetails

import com.bunbeauty.common.Constants.BULLET_SYMBOL
import com.bunbeauty.common.Constants.RUBLE_CURRENCY
import com.bunbeauty.common.Constants.X_SYMBOL
import com.bunbeauty.domain.model.cartproduct.OrderProduct

class OrderProductMapper {

    fun map(orderProduct: OrderProduct): OrderDetailsUiState.Product {
        return OrderDetailsUiState.Product(
            title = orderProduct.name,
            price = if (orderProduct.additionsPrice == null) {
                "${orderProduct.newPrice} $RUBLE_CURRENCY"
            } else {
                "(${orderProduct.newPrice} $RUBLE_CURRENCY + ${orderProduct.additionsPrice} $RUBLE_CURRENCY)"
            },
            count = "$X_SYMBOL ${orderProduct.count}",
            cost = "${orderProduct.newTotalCost} $RUBLE_CURRENCY",
            description = getDescription(orderProduct = orderProduct)
        )
    }

    private fun getDescription(orderProduct: OrderProduct): String? {
        val additions = getAdditionsString(orderProduct = orderProduct)
        return if (orderProduct.comboDescription == null && additions == null) {
            null
        } else {
            buildString {
                if (orderProduct.comboDescription != null) {
                    append(orderProduct.comboDescription)
                    if (additions != null) {
                        append("\n")
                    }
                }
                if (additions != null) {
                    append(additions)
                }
            }
        }
    }

    private fun getAdditionsString(orderProduct: OrderProduct): String? {
        return orderProduct.orderAdditions.takeIf { additions ->
            additions.isNotEmpty()
        }?.let { additions ->
            additions.joinToString(" $BULLET_SYMBOL ") { orderAddition ->
                orderAddition.name
            }
        }
    }
}
