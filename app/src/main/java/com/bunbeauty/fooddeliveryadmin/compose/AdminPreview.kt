package com.bunbeauty.fooddeliveryadmin.compose

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

@Preview(showSystemUi = true, group = "ScreenPreview")
annotation class ScreenPreview

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, group = "Dark")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, group = "Light")
annotation class ThemePreview
