package com.bunbeauty.ui_core.element

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bunbeauty.ui_core.FoodDeliveryTheme

@Composable
fun BlurLine(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(FoodDeliveryTheme.dimensions.blurHeight)
            .background(FoodDeliveryTheme.colors.surfaceGradient)
    )
}