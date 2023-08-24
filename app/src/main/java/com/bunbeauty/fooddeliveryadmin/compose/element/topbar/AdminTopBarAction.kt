package com.bunbeauty.fooddeliveryadmin.compose.element.topbar

import androidx.annotation.DrawableRes

data class AdminTopBarAction(
    @DrawableRes val iconId: Int,
    val onClick: () -> Unit
)
