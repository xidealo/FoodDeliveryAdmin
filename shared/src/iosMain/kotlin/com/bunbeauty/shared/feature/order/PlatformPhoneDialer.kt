package com.bunbeauty.shared.feature.order

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
actual fun rememberPhoneDialerLauncher(): (String) -> Unit =
    remember {
        { }
    }
