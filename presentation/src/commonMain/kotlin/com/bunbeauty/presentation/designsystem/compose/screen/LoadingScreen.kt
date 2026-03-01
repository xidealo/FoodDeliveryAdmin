package com.bunbeauty.presentation.designsystem.compose.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.bunbeauty.presentation.designsystem.compose.theme.AdminTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center),
            color = AdminTheme.colors.main.primary,
        )
    }
}

@Preview()
@Composable
private fun LoadingScreenPreview() {
    AdminTheme {
        LoadingScreen()
    }
}
