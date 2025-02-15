package com.bunbeauty.fooddeliveryadmin.compose.element.card

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme

object AdminCardDefaults {

    val cardColors: CardColors
        @Composable get() = CardDefaults.cardColors(
            containerColor = AdminTheme.colors.main.surface,
            disabledContainerColor = AdminTheme.colors.main.surface
        )

    val cardPositiveColors: CardColors
        @Composable get() = CardDefaults.cardColors(
            containerColor = AdminTheme.colors.status.positive
        )

    val cardNegativeColors: CardColors
        @Composable get() = CardDefaults.cardColors(
            containerColor = AdminTheme.colors.status.negative
        )

    val cardShape: RoundedCornerShape
        @Composable get() = RoundedCornerShape(AdminTheme.dimensions.cardRadius)

    val noCornerCardShape: RoundedCornerShape
        @Composable get() = RoundedCornerShape(0.dp)

    @Composable
    fun getCardElevation(elevated: Boolean): CardElevation = if (elevated) {
        CardDefaults.cardElevation(
            defaultElevation = AdminTheme.dimensions.cardElevation,
            disabledElevation = AdminTheme.dimensions.cardElevation
        )
    } else {
        CardDefaults.cardElevation(
            defaultElevation = 0.dp,
            disabledElevation = 0.dp
        )
    }
}
