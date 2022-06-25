package com.bunbeauty.ui_core

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp

fun Modifier.icon() = this.size(24.dp)
fun Modifier.smallIcon() = this.size(12.dp)

fun Modifier.card(hasShadow: Boolean) = composed {
    fillMaxWidth()
        .requiredHeightIn(min = FoodDeliveryTheme.dimensions.cardHeight)
        .shadow(
            elevation = FoodDeliveryTheme.dimensions.getEvaluation(hasShadow),
            shape = mediumRoundedCornerShape
        )
        .clip(mediumRoundedCornerShape)
}