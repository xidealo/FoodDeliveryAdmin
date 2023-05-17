package com.bunbeauty.fooddeliveryadmin.compose.element.top_bar

import androidx.annotation.DrawableRes

data class AdminTopBarAction(
    @DrawableRes val iconId: Int,
    val onClick: () -> Unit,
)