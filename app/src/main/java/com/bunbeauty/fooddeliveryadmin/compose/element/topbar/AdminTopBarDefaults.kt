package com.bunbeauty.fooddeliveryadmin.compose.element.topbar

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme

object AdminTopBarDefaults {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun topAppBarColors(
        containerColor: Color = AdminTheme.colors.main.surface,
        scrolledContainerColor: Color = AdminTheme.colors.main.surface,
        navigationIconContentColor: Color = AdminTheme.colors.main.onSurface,
        titleContentColor: Color = AdminTheme.colors.main.onSurface,
        actionIconContentColor: Color = AdminTheme.colors.main.onSurface,
    ) = TopAppBarDefaults.topAppBarColors(
        containerColor = containerColor,
        scrolledContainerColor = scrolledContainerColor,
        navigationIconContentColor = navigationIconContentColor,
        titleContentColor = titleContentColor,
        actionIconContentColor = actionIconContentColor,
    )
}
