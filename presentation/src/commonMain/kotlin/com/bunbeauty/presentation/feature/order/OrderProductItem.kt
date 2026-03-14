package com.bunbeauty.presentation.feature.order

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bunbeauty.presentation.designsystem.compose.element.card.AdminCard
import com.bunbeauty.presentation.designsystem.compose.theme.AdminTheme
import com.bunbeauty.presentation.designsystem.compose.theme.bold

@Composable
internal fun OrderProductItem(
    product: OrderDetailsViewState.Product,
    modifier: Modifier = Modifier,
) {
    AdminCard(
        modifier = modifier,
        clickable = false,
    ) {
        Column(
            modifier =
                Modifier
                    .padding(horizontal = 16.dp)
                    .padding(vertical = 8.dp),
        ) {
            Text(
                text = product.title,
                style = AdminTheme.typography.titleSmall.bold,
                color = AdminTheme.colors.main.onSurface,
            )

            product.description?.let { additions ->
                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = additions,
                    style = AdminTheme.typography.bodySmall,
                    color = AdminTheme.colors.main.onSurface,
                )
            }
            Row(
                modifier = Modifier.padding(top = 8.dp),
            ) {
                Text(
                    text = product.price,
                    style = AdminTheme.typography.bodySmall.bold,
                    color = AdminTheme.colors.main.onSurface,
                )
                Text(
                    modifier = Modifier.padding(start = 4.dp),
                    text = product.count,
                    style = AdminTheme.typography.bodySmall,
                    color = AdminTheme.colors.main.onSurface,
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = product.cost,
                    style = AdminTheme.typography.bodySmall.bold,
                    color = AdminTheme.colors.main.onSurface,
                )
            }
        }
    }
}
