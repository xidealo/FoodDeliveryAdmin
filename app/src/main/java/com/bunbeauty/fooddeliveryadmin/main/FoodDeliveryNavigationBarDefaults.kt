package com.bunbeauty.fooddeliveryadmin.main

import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme

object FoodDeliveryNavigationBarDefaults {

    @Composable
    fun navigationBarItemColors(): NavigationBarItemColors = NavigationBarItemDefaults.colors(
        selectedIconColor = AdminTheme.colors.mainColors.primary,
        selectedTextColor = AdminTheme.colors.mainColors.primary,
        indicatorColor = AdminTheme.colors.mainColors.surface,
        unselectedIconColor = AdminTheme.colors.mainColors.onSurfaceVariant,
        unselectedTextColor = AdminTheme.colors.mainColors.onSurfaceVariant,
    )
}
