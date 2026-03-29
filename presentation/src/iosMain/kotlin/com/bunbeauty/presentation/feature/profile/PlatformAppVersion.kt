package com.bunbeauty.presentation.feature.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import platform.Foundation.NSBundle

@Composable
actual fun rememberAppVersion(): String =
    remember {
        (NSBundle.mainBundle.objectForInfoDictionaryKey("CFBundleShortVersionString") as? String)
            .orEmpty()
    }
