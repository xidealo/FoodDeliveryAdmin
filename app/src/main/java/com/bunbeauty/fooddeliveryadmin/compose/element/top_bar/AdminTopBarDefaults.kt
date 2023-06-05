package com.bunbeauty.fooddeliveryadmin.compose.element.top_bar

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme

object AdminTopBarDefaults {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun topAppBarColors(
        containerColor: Color = AdminTheme.colors.mainColors.surface,
        scrolledContainerColor: Color = AdminTheme.colors.mainColors.surface,
        navigationIconContentColor: Color = AdminTheme.colors.mainColors.onSurface,
        titleContentColor: Color = AdminTheme.colors.mainColors.onSurface,
        actionIconContentColor: Color = AdminTheme.colors.mainColors.onSurface,
    ) = TopAppBarDefaults.topAppBarColors(
        containerColor = containerColor,
        scrolledContainerColor = scrolledContainerColor,
        navigationIconContentColor = navigationIconContentColor,
        titleContentColor = titleContentColor,
        actionIconContentColor = actionIconContentColor
    )
}
