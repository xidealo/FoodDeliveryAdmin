package com.bunbeauty.fooddeliveryadmin.screen.logout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentManager
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.compose.element.bottomsheet.AdminBottomSheet
import com.bunbeauty.fooddeliveryadmin.compose.element.bottomsheet.AdminModalBottomSheet
import com.bunbeauty.fooddeliveryadmin.compose.element.button.AdminButtonDefaults
import com.bunbeauty.fooddeliveryadmin.compose.element.button.MainButton
import com.bunbeauty.fooddeliveryadmin.compose.element.button.SecondaryButton
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme
import com.bunbeauty.presentation.feature.profile.Profile
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Composable
fun LogoutBottomSheet(
    isShown: Boolean,
    onAction: (Profile.Action) -> Unit,
) {
    AdminModalBottomSheet(
        title = stringResource(R.string.title_logout),
        onDismissRequest = {
            onAction(Profile.Action.LogoutCancel)
        },
        isShown = isShown
    ) {
        Column(verticalArrangement = Arrangement.Absolute.spacedBy(8.dp)) {
            MainButton(
                textStringId = R.string.action_common_logout,
                colors = AdminButtonDefaults.negativeButtonColors,
                onClick = {
                    onAction(Profile.Action.LogoutConfirm)
                },
            )
            SecondaryButton(
                elevated = false,
                textStringId = R.string.action_common_cancel,
                onClick = {
                    onAction(Profile.Action.LogoutCancel)
                },
            )
        }
    }
}

@Preview
@Composable
private fun ContentPreview() {
    AdminTheme {
        LogoutBottomSheet(
            isShown = true,
            onAction = {}
        )
    }
}

