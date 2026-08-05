package com.bunbeauty.shared.designsystem.compose.element.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bunbeauty.shared.designsystem.compose.theme.AdminTheme
import com.bunbeauty.shared.designsystem.compose.theme.bold
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun OrderProductCard(
    title: String,
    price: String,
    count: String,
    cost: String,
    modifier: Modifier = Modifier,
    description: String? = null,
) {
    AdminCard(
        modifier = modifier,
        clickable = false,
        elevated = false,
        colors = AdminCardDefaults.cardVariantColors,
        shape = AdminCardDefaults.smallCardShape,
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = title,
                    style = AdminTheme.typography.titleSmall.bold,
                    color = AdminTheme.colors.main.onSurface,
                )
                description?.let { text ->
                    Text(
                        text = text,
                        style = AdminTheme.typography.bodySmall,
                        color = AdminTheme.colors.main.onSurface,
                    )
                }
            }
            Row {
                Text(
                    text = price,
                    style = AdminTheme.typography.bodySmall.bold,
                    color = AdminTheme.colors.main.onSurface,
                )
                Text(
                    modifier = Modifier.padding(start = 4.dp),
                    text = count,
                    style = AdminTheme.typography.bodySmall,
                    color = AdminTheme.colors.main.onSurface,
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = cost,
                    style = AdminTheme.typography.bodySmall.bold,
                    color = AdminTheme.colors.main.onSurface,
                )
            }
        }
    }
}

@Preview
@Composable
private fun OrderProductCardPreview() {
    AdminTheme {
        OrderProductCard(
            modifier = Modifier.padding(AdminTheme.dimensions.mediumSpace),
            title = "Хот-дог французский с куриной колбаской",
            description = "Необычный лаваш • Добавка 1 • Добавка 2 • Добавка 3",
            price = "(99 ₽ + 100 ₽)",
            count = "× 2",
            cost = "198 ₽",
        )
    }
}
