package com.bunbeauty.shared.feature.order

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bunbeauty.shared.designsystem.compose.element.card.OrderProductCard

@Composable
internal fun OrderProductItem(
    product: OrderDetailsViewState.Product,
    modifier: Modifier = Modifier,
) {
    OrderProductCard(
        modifier = modifier,
        title = product.title,
        description = product.description,
        price = product.price,
        count = product.count,
        cost = product.cost,
    )
}
