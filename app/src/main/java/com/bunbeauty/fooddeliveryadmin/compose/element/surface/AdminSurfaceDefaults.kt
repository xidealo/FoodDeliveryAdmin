package com.bunbeauty.fooddeliveryadmin.compose.element.surface

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme

object AdminSurfaceDefaults {
    @Composable
    fun getSurfaceElevation(elevated: Boolean) =
        if (elevated) {
            AdminTheme.dimensions.surfaceElevation
        } else {
            0.dp
        }
}
