package com.bunbeauty.shared.designsystem.compose.element.topbar

import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bunbeauty.shared.designsystem.compose.theme.AdminTheme

@Composable
fun AdminHorizontalDivider(
    modifier: Modifier = Modifier,
    thickness: Dp = 1.dp,
    color: Color = AdminTheme.colors.main.stroke,
) {
    HorizontalDivider(
        modifier = modifier,
        thickness = thickness,
        color = color,
    )
}
