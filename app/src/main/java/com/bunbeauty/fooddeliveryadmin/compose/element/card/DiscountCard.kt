package com.bunbeauty.fooddeliveryadmin.compose.element.card

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme

@Composable
fun DiscountCard(discount: String) {
    AdminCard(
        colors = AdminCardDefaults.cardPositiveColors,
        shape = RoundedCornerShape(4.dp),
        elevated = false,
        clickable = false,
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 4.dp),
            text = discount,
            style = AdminTheme.typography.bodyMedium,
            color = AdminTheme.colors.status.onStatus,
        )
    }
}

@Preview
@Composable
fun DiscountCardPreview() {
    AdminTheme {
        DiscountCard(discount = "10%")
    }
}
