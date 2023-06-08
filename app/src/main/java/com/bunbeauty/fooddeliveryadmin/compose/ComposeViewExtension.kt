package com.bunbeauty.fooddeliveryadmin.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme

inline fun ComposeView.setContentWithTheme(crossinline content: @Composable () -> Unit) {
    setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
    setContent {
        AdminTheme {
            content()
        }
    }
}
