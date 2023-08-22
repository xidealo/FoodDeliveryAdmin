package com.bunbeauty.fooddeliveryadmin.compose.element.card

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme
import com.bunbeauty.fooddeliveryadmin.compose.theme.medium

@Composable
fun TextWithHintCard(
    label: String?,
    modifier: Modifier = Modifier,
    @StringRes hintStringId: Int? = null,
    hint: String = ""
) {
    AdminCard(
        modifier = modifier,
        clickable = false
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = AdminTheme.dimensions.mediumSpace,
                vertical = AdminTheme.dimensions.smallSpace
            ).fillMaxWidth()
        ) {
            val hintText = hintStringId?.let { id ->
                stringResource(id)
            } ?: hint
            Text(
                text = hintText,
                style = AdminTheme.typography.labelSmall.medium,
                color = AdminTheme.colors.main.onSurfaceVariant
            )
            Text(
                text = label ?: "",
                style = AdminTheme.typography.bodyMedium,
                color = AdminTheme.colors.main.onSurface
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
            hintStringId = R.string.hint_login_login,
            label = "+7 999 000-00-00"
        )
    }
}
