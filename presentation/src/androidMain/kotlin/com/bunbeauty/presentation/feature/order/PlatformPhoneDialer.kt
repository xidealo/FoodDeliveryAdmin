package com.bunbeauty.presentation.feature.order

import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import coil3.compose.LocalPlatformContext

@Composable
actual fun rememberPhoneDialerLauncher(): (String) -> Unit {
    val context = LocalPlatformContext.current

    return remember(context) {
        { phoneNumber ->
            val dialIntent =
                Intent(
                    Intent.ACTION_DIAL,
                    Uri.parse("tel:$phoneNumber"),
                ).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }

            context.startActivity(dialIntent)
        }
    }
}
