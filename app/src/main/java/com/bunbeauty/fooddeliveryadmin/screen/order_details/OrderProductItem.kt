package com.bunbeauty.fooddeliveryadmin.screen.order_details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bunbeauty.fooddeliveryadmin.compose.element.card.AdminCard
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme
import com.bunbeauty.fooddeliveryadmin.compose.theme.bold
import com.bunbeauty.presentation.feature.order.state.OrderDetailsUiState

@Composable
fun OrderProductItem(
    product: OrderDetailsUiState.Product,
    modifier: Modifier = Modifier,
) {
    AdminCard(
        modifier = modifier,
        clickable = false,
    ) {
        Row(modifier = Modifier.padding(8.dp)) {
            Text(
                modifier = Modifier.weight(1f),
                text = product.title,
                style = AdminTheme.typography.titleSmall.bold,
                color = AdminTheme.colors.main.onSurface,
            )
            Column(
                modifier = Modifier.padding(start = 8.dp),
                horizontalAlignment = Alignment.End
            ) {
                Row {
                    Text(
                        text = product.price,
                        style = AdminTheme.typography.bodySmall.bold,
                        color = AdminTheme.colors.main.onSurface,
                    )
                    Text(
                        modifier = Modifier.padding(start = 8.dp),
                        text = product.count,
                        style = AdminTheme.typography.bodySmall,
                        color = AdminTheme.colors.main.onSurface,
                    )
                }
                Text(
                    modifier = Modifier.padding(top = 8.dp),
                    text = product.cost,
                    style = AdminTheme.typography.bodySmall.bold,
                    color = AdminTheme.colors.main.onSurface,
                )
            }
        }
    }
}

@Preview
@Composable
private fun OrderProductItemPreview() {
    OrderProductItem(
        product = OrderDetailsUiState.Product(
            title = "Хот-дог французский с куриной колбаской",
            price = "99 ₽",
            count = "× 2",
            cost = "198 ₽",
        )
    )
}
