package com.bunbeauty.fooddeliveryadmin.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.bunbeauty.fooddeliveryadmin.view.theme.FoodDeliveryTheme

@Composable
fun FoodDeliveryToolbarScreen(
    modifier: Modifier,
    title: String? = null,
    backgroundColor: Color = FoodDeliveryTheme.colors.background,
    content: @Composable BoxScope.() -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    title?.let {
                        Text(text = title)
                    }
                },
                backgroundColor = FoodDeliveryTheme.colors.surface,
                contentColor = FoodDeliveryTheme.colors.onSurface,
            )
        },
        backgroundColor = backgroundColor
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            content = content
        )
    }
}
