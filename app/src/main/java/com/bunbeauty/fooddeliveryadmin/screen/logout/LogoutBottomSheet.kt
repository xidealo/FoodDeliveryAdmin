package com.bunbeauty.fooddeliveryadmin.screen.logout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentManager
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.compose.element.bottomsheet.AdminBottomSheet
import com.bunbeauty.fooddeliveryadmin.compose.element.button.AdminButtonDefaults
import com.bunbeauty.fooddeliveryadmin.compose.element.button.MainButton
import com.bunbeauty.fooddeliveryadmin.compose.element.button.SecondaryButton
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme
import com.bunbeauty.fooddeliveryadmin.coreui.ComposeBottomSheet
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class LogoutBottomSheet : ComposeBottomSheet<Boolean>() {
    @Composable
    override fun Content() {
        AdminBottomSheet(titleStringId = R.string.title_logout) {
            Column(verticalArrangement = Arrangement.Absolute.spacedBy(8.dp)) {
                MainButton(
                    textStringId = R.string.action_common_logout,
                    colors = AdminButtonDefaults.negativeButtonColors,
                    onClick = {
                        callback?.onResult(true)
                        dismiss()
                    },
                )
                SecondaryButton(
                    elevated = false,
                    textStringId = R.string.action_common_cancel,
                    onClick = {
                        callback?.onResult(false)
                        dismiss()
                    },
                )
            }
        }
    }

    @Preview
    @Composable
    private fun ContentPreview() {
        AdminTheme {
            Content()
        }
    }

    companion object {
        private const val TAG = "LogoutBottomSheet"

        suspend fun show(fragmentManager: FragmentManager) =
            suspendCoroutine { continuation ->
                LogoutBottomSheet().apply {
                    callback =
                        object : Callback<Boolean> {
                            override fun onResult(result: Boolean?) {
                                continuation.resume(result)
                            }
                        }
                    show(fragmentManager, TAG)
                }
            }
    }
}
