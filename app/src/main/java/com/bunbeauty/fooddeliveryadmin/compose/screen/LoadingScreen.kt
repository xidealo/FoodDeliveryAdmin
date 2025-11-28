package com.bunbeauty.fooddeliveryadmin.compose.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme

@Composable
internal fun LoadingScreen(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center),
            color = AdminTheme.colors.main.primary,
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun LoadingScreenPreview() {
    AdminTheme {
        LoadingScreen()
    }
}
