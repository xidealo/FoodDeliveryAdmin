package com.bunbeauty.presentation.designsystem.compose.element.topbar

import androidx.compose.ui.graphics.Color

data class AdminTopBarAction(
    val iconId: Int,
    val onClick: () -> Unit,
    val color: Color,
)
