package com.bunbeauty.shared.feature.profile

import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import coil3.compose.LocalPlatformContext

@Composable
actual fun rememberAppVersion(): String {
    val context = LocalPlatformContext.current

    return remember(context) {
        runCatching {
            val packageInfo =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    context.packageManager.getPackageInfo(
                        context.packageName,
                        PackageManager.PackageInfoFlags.of(0),
                    )
                } else {
                    @Suppress("DEPRECATION")
                    context.packageManager.getPackageInfo(context.packageName, 0)
                }

            packageInfo.versionName.orEmpty()
        }.getOrDefault("")
    }
}
