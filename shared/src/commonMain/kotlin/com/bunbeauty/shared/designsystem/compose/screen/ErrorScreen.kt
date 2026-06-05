package com.bunbeauty.shared.designsystem.compose.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bunbeauty.shared.designsystem.compose.bottomBarPadding
import com.bunbeauty.shared.designsystem.compose.element.button.LoadingButton
import com.bunbeauty.shared.designsystem.compose.theme.AdminTheme
import com.bunbeauty.shared.designsystem.compose.theme.bold
import fooddeliveryadmin.shared.generated.resources.Res
import fooddeliveryadmin.shared.generated.resources.action_retry
import fooddeliveryadmin.shared.generated.resources.ic_error
import fooddeliveryadmin.shared.generated.resources.msg_common_check_connection_and_retry
import fooddeliveryadmin.shared.generated.resources.title_common_can_not_load_data
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ErrorScreen(
    mainTextId: StringResource,
    isLoading: Boolean = false,
    extraTextId: StringResource? = null,
    onClick: () -> Unit,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.weight(1f))
        Box(
            modifier =
                Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(AdminTheme.colors.main.error),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                modifier = Modifier.size(64.dp),
                painter = painterResource(Res.drawable.ic_error),
                tint = AdminTheme.colors.main.onError,
                contentDescription = null,
            )
        }
        Text(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp)
                    .padding(horizontal = AdminTheme.dimensions.mediumSpace),
            text = stringResource(resource = mainTextId),
            style = AdminTheme.typography.titleMedium.bold,
            color = AdminTheme.colors.main.onSurface,
            textAlign = TextAlign.Center,
        )
        extraTextId?.let {
            Text(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(top = AdminTheme.dimensions.smallSpace)
                        .padding(horizontal = AdminTheme.dimensions.mediumSpace),
                text = stringResource(resource = extraTextId),
                style = AdminTheme.typography.bodyLarge,
                color = AdminTheme.colors.main.onSurface,
                textAlign = TextAlign.Center,
            )
        }
        Spacer(modifier = Modifier.weight(1f))

        LoadingButton(
            modifier =
                Modifier
                    .padding(bottom = AdminTheme.dimensions.mediumSpace)
                    .padding(horizontal = AdminTheme.dimensions.mediumSpace)
                    .bottomBarPadding(),
            text = stringResource(Res.string.action_retry),
            isLoading = isLoading,
            onClick = onClick,
        )
    }
}

@Preview()
@Composable
private fun ErrorScreenPreview() {
    AdminTheme {
        ErrorScreen(
            mainTextId = Res.string.title_common_can_not_load_data,
            extraTextId = Res.string.msg_common_check_connection_and_retry,
            onClick = {},
        )
    }
}
