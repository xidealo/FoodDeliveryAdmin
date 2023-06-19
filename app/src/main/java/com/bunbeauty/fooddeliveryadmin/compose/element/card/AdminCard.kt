package com.bunbeauty.fooddeliveryadmin.compose.element.card

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import com.bunbeauty.fooddeliveryadmin.compose.element.rememberMultipleEventsCutter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminCard(
    modifier: Modifier = Modifier,
    elevated: Boolean = true,
    onClick: (() -> Unit) = {},
    clickable: Boolean = true,
    colors: CardColors = AdminCardDefaults.cardColors,
    shape: Shape = AdminCardDefaults.cardShape,
    border: BorderStroke? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    CompositionLocalProvider(
        LocalMinimumInteractiveComponentEnforcement provides false,
    ) {
        if (clickable) {
            val multipleEventsCutter = rememberMultipleEventsCutter()
            Card(
                modifier = modifier,
                shape = shape,
                colors = colors,
                elevation = AdminCardDefaults.getCardElevation(elevated),
                onClick = {
                    multipleEventsCutter.processEvent(onClick)
                },
                border = border,
                content = content
            )
        } else {
            Card(
                modifier = modifier,
                shape = shape,
                colors = colors,
                elevation = AdminCardDefaults.getCardElevation(elevated),
                border = border,
                content = content
            )
        }
    }
}
