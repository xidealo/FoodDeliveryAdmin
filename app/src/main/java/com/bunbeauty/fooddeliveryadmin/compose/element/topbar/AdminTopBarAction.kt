package com.bunbeauty.fooddeliveryadmin.compose.element.topbar

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color

data class AdminTopBarAction(
    @DrawableRes val iconId: Int,
    val onClick: () -> Unit,
    val color: Color,
)
