package com.bunbeauty.presentation.feature.profile.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.bunbeauty.presentation.designsystem.compose.element.bottomsheet.AdminModalBottomSheet
import com.bunbeauty.presentation.designsystem.compose.element.button.AdminButtonDefaults
import com.bunbeauty.presentation.designsystem.compose.element.button.MainButton
import com.bunbeauty.presentation.designsystem.compose.element.button.SecondaryButton
import com.bunbeauty.presentation.designsystem.compose.theme.AdminTheme
import com.bunbeauty.presentation.feature.profile.Profile
import fooddeliveryadmin.presentation.generated.resources.Res
import fooddeliveryadmin.presentation.generated.resources.action_common_cancel
import fooddeliveryadmin.presentation.generated.resources.action_common_logout
import fooddeliveryadmin.presentation.generated.resources.title_logout
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun LogoutBottomSheet(
    isShown: Boolean,
    onAction: (Profile.Action) -> Unit,
) {
    AdminModalBottomSheet(
        title = stringResource(Res.string.title_logout),
        onDismissRequest = {
            onAction(Profile.Action.LogoutCancel)
        },
        isShown = isShown,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            MainButton(
                textStringId = Res.string.action_common_logout,
                colors = AdminButtonDefaults.negativeButtonColors,
                onClick = {
                    onAction(Profile.Action.LogoutConfirm)
                },
            )
            SecondaryButton(
                elevated = false,
                textStringId = Res.string.action_common_cancel,
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
            onAction = {},
        )
    }
}
