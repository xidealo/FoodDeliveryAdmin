package com.bunbeauty.fooddeliveryadmin.compose.element.surface

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.zIndex
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme

@Composable
fun AdminSurface(
    modifier: Modifier = Modifier,
    color: Color = AdminTheme.colors.main.surface,
    elevated: Boolean = true,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier.zIndex(1f),
        color = color,
        shadowElevation = AdminSurfaceDefaults.getSurfaceElevation(elevated),
        content = content
    )
}
