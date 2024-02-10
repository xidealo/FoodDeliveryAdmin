package com.bunbeauty.fooddeliveryadmin.screen.orderdetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bunbeauty.fooddeliveryadmin.compose.element.card.AdminCard
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme
import com.bunbeauty.fooddeliveryadmin.compose.theme.bold

@Composable
fun OrderProductItem(
    product: OrderDetailsUiState.Product,
    modifier: Modifier = Modifier
) {
    AdminCard(
        modifier = modifier,
        clickable = false
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(vertical = 8.dp)
        ) {
            Text(
                text = product.title,
                style = AdminTheme.typography.titleSmall.bold,
                color = AdminTheme.colors.main.onSurface
            )

            product.additions?.let { additions ->
                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = additions,
                    style = AdminTheme.typography.bodySmall,
                    color = AdminTheme.colors.main.onSurface
                )
            }
            Row(
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text(
                    text = product.price,
                    style = AdminTheme.typography.bodySmall.bold,
                    color = AdminTheme.colors.main.onSurface
                )
                Text(
                    modifier = Modifier.padding(start = 4.dp),
                    text = product.count,
                    style = AdminTheme.typography.bodySmall,
                    color = AdminTheme.colors.main.onSurface
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = product.cost,
                    style = AdminTheme.typography.bodySmall.bold,
                    color = AdminTheme.colors.main.onSurface
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
            additions = "Необычный лаваш • Добавка 1 • Добавка 2"
        )
    )
}
