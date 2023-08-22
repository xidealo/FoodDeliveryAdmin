package com.bunbeauty.presentation.feature.order.mapper

import android.content.res.Resources
import com.bunbeauty.domain.model.cart_product.OrderProduct
import com.bunbeauty.presentation.R
import com.bunbeauty.presentation.feature.order.state.OrderDetailsUiState
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
            price = resources.getString(
                R.string.common_with_ruble,
                orderProduct.newPrice.toString()
            ),
            count = resources.getString(R.string.common_with_pieces, orderProduct.count.toString()),
            cost = resources.getString(
                R.string.common_with_ruble,
                (orderProduct.newPrice * orderProduct.count).toString()
            )
        )
    }
}