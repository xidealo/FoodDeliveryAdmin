package com.bunbeauty.shared.designsystem.compose.element.card

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bunbeauty.shared.designsystem.compose.theme.AdminTheme
import fooddeliveryadmin.shared.generated.resources.Res
import fooddeliveryadmin.shared.generated.resources.ic_warning
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun WarningCard(
    title: String,
    icon: DrawableResource,
    iconDescription: String,
    modifier: Modifier = Modifier,
    cardColors: CardColors = AdminCardDefaults.warningCardStatusColors,
) {
    AdminCard(
        modifier = modifier,
        colors = cardColors,
        elevated = false,
        clickable = false,
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(
                        vertical = 12.dp,
                        horizontal = 16.dp,
                    ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(icon),
                tint = AdminTheme.colors.status.onStatus,
                contentDescription = iconDescription,
            )
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = title,
                style = AdminTheme.typography.bodyLarge,
                color = AdminTheme.colors.status.onStatus,
            )
        }
    }
}

@Preview
@Composable
private fun WarningCardPreview() {
    AdminTheme {
        WarningCard(
            title = "Проблемный клиент",
            icon = Res.drawable.ic_warning,
            iconDescription = "Иконка",
        )
    }
}
