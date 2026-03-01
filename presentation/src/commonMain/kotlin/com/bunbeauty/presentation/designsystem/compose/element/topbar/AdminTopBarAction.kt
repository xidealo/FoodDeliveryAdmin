package com.bunbeauty.presentation.designsystem.compose.element.topbar

import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.resources.DrawableResource

data class AdminTopBarAction(
    val iconId: DrawableResource,
    val onClick: () -> Unit,
    val color: Color,
)
