package com.bunbeauty.shared.designsystem.compose.element.surface

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.zIndex
import com.bunbeauty.shared.designsystem.compose.element.card.AdminCardDefaults
import com.bunbeauty.shared.designsystem.compose.theme.AdminTheme

@Composable
fun AdminSurface(
    modifier: Modifier = Modifier,
    color: Color = AdminTheme.colors.main.surface,
    elevated: Boolean = true,
    shape: Shape = AdminCardDefaults.cardShape,
    border: BorderStroke? = null,
    content: @Composable () -> Unit,
) {
    Surface(
        shape = shape,
        border = border,
        modifier = modifier.zIndex(1f),
        color = color,
        shadowElevation = AdminSurfaceDefaults.getSurfaceElevation(elevated),
        content = content,
    )
}
