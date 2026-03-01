package com.bunbeauty.presentation.designsystem.compose

import android.content.res.Configuration
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview(showSystemUi = true, group = "ScreenPreview")
annotation class ScreenPreview

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, group = "Dark")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, group = "Light")
annotation class ThemePreview
