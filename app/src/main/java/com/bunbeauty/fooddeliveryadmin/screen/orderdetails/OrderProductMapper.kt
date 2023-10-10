package com.bunbeauty.fooddeliveryadmin.screen.orderdetails

import android.content.res.Resources
import com.bunbeauty.domain.model.cartproduct.OrderProduct
import com.bunbeauty.presentation.R
import javax.inject.Inject

class OrderProductMapper @Inject constructor(
    private val resources: Resources
) {

    fun map(orderProduct: OrderProduct): OrderDetailsUiState.Product {
        return OrderDetailsUiState.Product(
            title = if (orderProduct.comboDescription == null) {
                orderProduct.name
            } else {
                orderProduct.name + "\n" + (orderProduct.comboDescription ?: "")
            },
            price = if(orderProduct.additionsPrice == null){
                resources.getString(
                    R.string.common_with_ruble,
                    orderProduct.newPrice.toString()
                )
            }else{
                resources.getString(
                    R.string.hint_order_details_pickup_deferred_time,
                    orderProduct.newPrice.toString(),
                    orderProduct.additionsPrice.toString()
                )
            },
            count = resources.getString(R.string.common_with_pieces, orderProduct.count.toString()),
            cost = resources.getString(
                R.string.common_with_ruble,
                orderProduct.newTotalCost.toString()
            ),
            additions = orderProduct.additions.joinToString(" • ") { orderAddition ->
                orderAddition.name
            }.ifBlank {
                null
            }
        )
    }
}