package com.bunbeauty.shared.feature.order

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import platform.Foundation.NSURL
import platform.UIKit.UIApplication

@Composable
actual fun rememberPhoneDialerLauncher(): (String) -> Unit =
    remember {
        { phoneNumber ->
            val url = NSURL.URLWithString("tel:$phoneNumber")
            if (url != null) {
                UIApplication.sharedApplication.openURL(
                    url = url,
                    options = emptyMap<Any?, Any?>(),
                    completionHandler = null,
                )
            }
        }
    }
