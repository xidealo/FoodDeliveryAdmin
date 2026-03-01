package com.bunbeauty.presentation.designsystem.compose.element.card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.bunbeauty.presentation.designsystem.compose.theme.AdminTheme
import com.bunbeauty.presentation.designsystem.compose.theme.medium
import fooddeliveryadmin.presentation.generated.resources.Res
import fooddeliveryadmin.presentation.generated.resources.description_common_navigate
import fooddeliveryadmin.presentation.generated.resources.hint_login_login
import fooddeliveryadmin.presentation.generated.resources.ic_right_arrow
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun StatusNavigationTextCard(
    statusColor: Color,
    modifier: Modifier = Modifier,
    hintStringId: StringResource,
    label: String?,
    clickable: Boolean = true,
    onClick: () -> Unit,
) {
    AdminCard(
        modifier = modifier,
        onClick = onClick,
        clickable = clickable,
    ) {
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
            Spacer(
                modifier =
                    Modifier
                        .fillMaxHeight()
                        .width(8.dp)
                        .background(statusColor),
            )
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, end = 16.dp)
                        .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    modifier =
                        Modifier
                            .weight(1f)
                            .padding(end = AdminTheme.dimensions.smallSpace),
                ) {
                    Text(
                        text = stringResource(hintStringId),
                        style = AdminTheme.typography.labelSmall.medium,
                        color = AdminTheme.colors.main.onSurfaceVariant,
                    )
                    Text(
                        text = label.orEmpty(),
                        style = AdminTheme.typography.bodyMedium,
                        color = AdminTheme.colors.main.onSurface,
                    )
                }
                Icon(
                    modifier = Modifier.size(16.dp),
                    painter = painterResource(Res.drawable.ic_right_arrow),
                    tint = AdminTheme.colors.main.onSurfaceVariant,
                    contentDescription = stringResource(Res.string.description_common_navigate),
                )
            }
        }
    }
}

@Preview
@Composable
private fun NavigationIconCardPreview() {
    AdminTheme {
        StatusNavigationTextCard(
            statusColor = AdminTheme.colors.order.notAccepted,
            modifier = Modifier.padding(AdminTheme.dimensions.mediumSpace),
            hintStringId = Res.string.hint_login_login,
            label = "+7 999 000-00-00",
            onClick = {},
        )
    }
}
