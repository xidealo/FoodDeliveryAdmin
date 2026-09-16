package com.bunbeauty.shared.feature.order

import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import coil3.compose.LocalPlatformContext

@Composable
actual fun rememberMapRouteLauncher(): (String) -> Unit {
    val context = LocalPlatformContext.current

    return remember(context) {
        { mapQuery ->
            val mapIntent =
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(buildGoogleMapsDirUrl(mapQuery)),
                ).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }

            context.startActivity(mapIntent)
        }
    }
}
