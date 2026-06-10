package com.bunbeauty.shared.feature.profile.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.bunbeauty.shared.designsystem.compose.element.bottomsheet.AdminModalBottomSheet
import com.bunbeauty.shared.designsystem.compose.element.button.AdminButtonDefaults
import com.bunbeauty.shared.designsystem.compose.element.button.LoadingButton
import com.bunbeauty.shared.designsystem.compose.element.button.SecondaryButton
import com.bunbeauty.shared.designsystem.compose.provider.BooleanPreviewParameterProvider
import com.bunbeauty.shared.designsystem.compose.theme.AdminTheme
import com.bunbeauty.shared.feature.profile.Profile
import fooddeliveryadmin.shared.generated.resources.Res
import fooddeliveryadmin.shared.generated.resources.action_common_cancel
import fooddeliveryadmin.shared.generated.resources.action_common_logout
import fooddeliveryadmin.shared.generated.resources.title_logout
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.ui.tooling.preview.PreviewParameter

@Composable
fun LogoutBottomSheet(
    isShown: Boolean,
    isLoading: Boolean,
    onAction: (Profile.Action) -> Unit,
) {
    AdminModalBottomSheet(
        title = stringResource(Res.string.title_logout),
        onDismissRequest = {
            if (!isLoading) {
                onAction(Profile.Action.LogoutCancel)
            }
        },
        isShown = isShown,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            LoadingButton(
                text = stringResource(Res.string.action_common_logout),
                colors = AdminButtonDefaults.negativeButtonColors,
                isLoading = isLoading,
                onClick = {
                    onAction(Profile.Action.LogoutConfirm)
                },
            )
            SecondaryButton(
                elevated = false,
                textStringId = Res.string.action_common_cancel,
                isEnabled = !isLoading,
                onClick = {
                    onAction(Profile.Action.LogoutCancel)
                },
            )
        }
    }
}

@Preview
@Composable
private fun ContentPreview(
    @PreviewParameter(BooleanPreviewParameterProvider::class)
    isLoading: Boolean,
) {
    AdminTheme {
        LogoutBottomSheet(
            isShown = true,
            isLoading = isLoading,
            onAction = {},
        )
    }
}
