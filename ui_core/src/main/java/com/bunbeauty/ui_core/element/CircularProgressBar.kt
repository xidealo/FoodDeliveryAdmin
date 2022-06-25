package com.bunbeauty.ui_core.element

import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bunbeauty.ui_core.FoodDeliveryTheme

@Composable
fun CircularProgressBar(
    modifier: Modifier = Modifier
) {
    CircularProgressIndicator(
        modifier = modifier,
        color = FoodDeliveryTheme.colors.primary
    )
}