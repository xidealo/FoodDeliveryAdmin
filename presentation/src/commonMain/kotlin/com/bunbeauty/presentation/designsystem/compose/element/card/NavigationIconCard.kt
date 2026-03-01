package com.bunbeauty.presentation.designsystem.compose.element.card

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.bunbeauty.presentation.designsystem.compose.theme.AdminTheme
import fooddeliveryadmin.presentation.generated.resources.Res
import fooddeliveryadmin.presentation.generated.resources.description_common_navigate
import fooddeliveryadmin.presentation.generated.resources.ic_menu
import fooddeliveryadmin.presentation.generated.resources.ic_right_arrow
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun NavigationIconCard(
    modifier: Modifier = Modifier,
    iconId: DrawableResource,
    iconDescriptionStringId: StringResource? = null,
    labelStringId: StringResource? = null,
    label: String = "",
    elevated: Boolean = true,
    onClick: () -> Unit,
) {
    AdminCard(
        modifier = modifier,
        onClick = onClick,
        elevated = elevated,
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 16.dp,
                        vertical = 12.dp,
                    ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(iconId),
                tint = AdminTheme.colors.main.onSurfaceVariant,
                contentDescription =
                    iconDescriptionStringId?.let { stringId ->
                        stringResource(stringId)
                    },
            )
            val labelText =
                labelStringId?.let { id ->
                    stringResource(id)
                } ?: label
            Text(
                modifier =
                    Modifier
                        .padding(horizontal = AdminTheme.dimensions.mediumSpace)
                        .weight(1f),
                text = labelText,
                style = AdminTheme.typography.bodyLarge,
                color = AdminTheme.colors.main.onSurface,
                overflow = TextOverflow.Ellipsis,
            )
            Icon(
                modifier = Modifier.size(16.dp),
                painter = painterResource(Res.drawable.ic_right_arrow),
                tint = AdminTheme.colors.main.onSurfaceVariant,
                contentDescription = stringResource(Res.string.description_common_navigate),
            )
        }
    }
}

@Preview
@Composable
private fun NavigationIconCardPreview() {
    AdminTheme {
        NavigationIconCard(
            iconId = Res.drawable.ic_menu,
            label = "Текст",
            onClick = {},
        )
    }
}
