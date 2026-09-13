package com.bunbeauty.shared.feature.order

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import platform.Foundation.NSURL
import platform.UIKit.UIApplication

@Composable
actual fun rememberMapRouteLauncher(): (String) -> Unit =
    remember {
        { mapQuery ->
            val url = NSURL.URLWithString(buildGoogleMapsDirUrl(mapQuery))
            if (url != null) {
                UIApplication.sharedApplication.openURL(
                    url = url,
                    options = emptyMap<Any?, Any?>(),
                    completionHandler = null,
                )
            }
        }
    }
