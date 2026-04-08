package com.bunbeauty.shared.designsystem.compose.element.card

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bunbeauty.shared.designsystem.compose.theme.AdminTheme
import com.bunbeauty.shared.designsystem.compose.theme.medium
import fooddeliveryadmin.shared.generated.resources.Res
import fooddeliveryadmin.shared.generated.resources.hint_login_login
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun TextWithHintCard(
    label: String?,
    modifier: Modifier = Modifier,
    hintStringId: StringResource? = null,
    hint: String = "",
) {
    AdminCard(
        modifier = modifier,
        clickable = false,
    ) {
        Column(
            modifier =
                Modifier
                    .padding(
                        horizontal = 16.dp,
                        vertical = 8.dp,
                    ).fillMaxWidth(),
        ) {
            val hintText =
                hintStringId?.let { id ->
                    stringResource(id)
                } ?: hint
            Text(
                text = hintText,
                style = AdminTheme.typography.labelSmall.medium,
                color = AdminTheme.colors.main.onSurfaceVariant,
            )
            Text(
                text = label ?: "",
                style = AdminTheme.typography.bodyMedium,
                color = AdminTheme.colors.main.onSurface,
            )
        }
    }
}

@Preview
@Composable
private fun TextNavigationCardPreview() {
    AdminTheme {
        TextWithHintCard(
            modifier = Modifier.padding(AdminTheme.dimensions.mediumSpace),
            hintStringId = Res.string.hint_login_login,
            label = "+7 999 000-00-00",
        )
    }
}
