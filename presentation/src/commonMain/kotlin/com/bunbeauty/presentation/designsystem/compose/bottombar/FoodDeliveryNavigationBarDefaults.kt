package com.bunbeauty.presentation.designsystem.compose.bottombar

import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import com.bunbeauty.presentation.designsystem.compose.theme.AdminTheme

object FoodDeliveryNavigationBarDefaults {
    @Composable
    fun navigationBarItemColors(): NavigationBarItemColors =
        NavigationBarItemDefaults.colors(
            selectedIconColor = AdminTheme.colors.main.primary,
            selectedTextColor = AdminTheme.colors.main.primary,
            indicatorColor = AdminTheme.colors.main.surface,
            unselectedIconColor = AdminTheme.colors.main.onSurfaceVariant,
            unselectedTextColor = AdminTheme.colors.main.onSurfaceVariant,
        )
}