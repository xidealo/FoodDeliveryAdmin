package com.bunbeauty.fooddeliveryadmin.compose.theme

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AdminTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors =
        if (isDarkTheme) {
            LightAdminColors
        } else {
            LightAdminColors
        }
    val rememberedColors =
        remember {
            colors.copy()
        }.apply {
            update(colors)
        }

    CompositionLocalProvider(
        LocalOverscrollConfiguration provides null,
        LocalAdminColors provides rememberedColors,
        LocalAdminDimensions provides AdminDimensions(),
        LocalAdminTypography provides AdminTypography(),
        content = content,
    )
}

object AdminTheme {
    val colors: AdminColors
        @Composable
        @ReadOnlyComposable
        get() = LocalAdminColors.current
    val typography: AdminTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalAdminTypography.current
    val dimensions: AdminDimensions
        @Composable
        @ReadOnlyComposable
        get() = LocalAdminDimensions.current
}
