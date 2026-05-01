package com.bunbeauty.shared.feature.order

import androidx.compose.runtime.Composable

@Composable
expect fun rememberPhoneDialerLauncher(): (String) -> Unit
