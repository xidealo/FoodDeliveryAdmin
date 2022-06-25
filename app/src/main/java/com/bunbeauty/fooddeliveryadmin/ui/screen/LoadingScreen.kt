package com.bunbeauty.fooddeliveryadmin.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.bunbeauty.ui_core.FoodDeliveryTheme

@Composable
internal fun LoadingScreen(
    background: Color = FoodDeliveryTheme.colors.background
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
    ) {
        CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center),
            color = FoodDeliveryTheme.colors.primary
        )
    }
}


@Preview(showSystemUi = true)
@Composable
private fun LoadingScreenPreview() {
    LoadingScreen()
}