package com.bunbeauty.fooddeliveryadmin.screen.logout

import android.os.Bundle
import android.view.View
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentManager
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.compose.element.bottom_sheet.AdminBottomSheet
import com.bunbeauty.fooddeliveryadmin.compose.element.button.MainButton
import com.bunbeauty.fooddeliveryadmin.compose.element.button.SecondaryButton
import com.bunbeauty.fooddeliveryadmin.compose.setContentWithTheme
import com.bunbeauty.fooddeliveryadmin.core_ui.ComposeBottomSheet
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class LogoutBottomSheet : ComposeBottomSheet<Boolean>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.root.setContentWithTheme {
            LogoutScreen(
                onLogoutClicked = {
                    callback?.onResult(true)
                    dismiss()
                },
                onCancelClicked = {
                    callback?.onResult(false)
                    dismiss()
                }
            )
        }
    }

    companion object {
        private const val TAG = "LogoutBottomSheet"

        suspend fun show(
            fragmentManager: FragmentManager,
        ) = suspendCoroutine { continuation ->
            LogoutBottomSheet().apply {
                callback = object : Callback<Boolean> {
                    override fun onResult(result: Boolean?) {
                        continuation.resume(result)
                    }
                }
                show(fragmentManager, TAG)
            }
        }
    }
}

@Composable
private fun LogoutScreen(
    onLogoutClicked: () -> Unit,
    onCancelClicked: () -> Unit,
) {
    AdminBottomSheet(titleStringId = R.string.title_logout) {
        Column(verticalArrangement = Arrangement.Absolute.spacedBy(8.dp)) {
            MainButton(
                textStringId = R.string.action_common_logout,
                onClick = onLogoutClicked
            )
            SecondaryButton(
                textStringId = R.string.action_common_cancel,
                onClick = onCancelClicked
            )
        }
    }
}

@Preview
@Composable
private fun LogoutScreenPreview() {
    LogoutScreen(
        onLogoutClicked = {},
        onCancelClicked = {},
    )
}
