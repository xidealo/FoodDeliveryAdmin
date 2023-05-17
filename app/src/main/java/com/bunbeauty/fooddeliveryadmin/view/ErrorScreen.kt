package com.bunbeauty.fooddeliveryadmin.view

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme
import com.bunbeauty.fooddeliveryadmin.compose.theme.bold

@Composable
fun ErrorScreen(
    @StringRes mainTextId: Int,
    @DrawableRes iconId: Int,
    @StringRes extraTextId: Int? = null,
    action: () -> Unit,
) {
    Column(
        modifier = Modifier
            .background(
                color = AdminTheme.colors.mainColors.background
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(
            modifier = Modifier
                .weight(1f)
        )

        Icon(
            modifier = Modifier
                .size(40.dp),
            imageVector = ImageVector
                .vectorResource(iconId),
            contentDescription = null,
            tint = AdminTheme.colors.mainColors.primary
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = AdminTheme.dimensions.mediumSpace)
                .padding(horizontal = AdminTheme.dimensions.mediumSpace),
            text = stringResource(id = mainTextId),
            style = AdminTheme.typography.titleMedium.bold,
            color = AdminTheme.colors.mainColors.onBackground,
            textAlign = TextAlign.Center
        )

        extraTextId?.let {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = AdminTheme.dimensions.smallSpace)
                    .padding(horizontal = AdminTheme.dimensions.mediumSpace),
                text = stringResource(id = extraTextId),
                style = AdminTheme.typography.bodyLarge,
                color = AdminTheme.colors.mainColors.onBackground,
                textAlign = TextAlign.Center
            )
        }

        Spacer(
            modifier = Modifier
                .weight(1f)
        )

        TextButton(
            modifier = Modifier
                .padding(bottom = AdminTheme.dimensions.mediumSpace),
            onClick = action
        ) {
            Text(
                color = AdminTheme.colors.mainColors.onBackground,
                text = stringResource(id = com.bunbeauty.fooddeliveryadmin.R.string.action_retry),
                style = AdminTheme.typography.labelLarge
            )
        }
    }
}

@Preview
@Composable
private fun TaskErrorPreview() {
    ErrorScreen(
        mainTextId = com.bunbeauty.fooddeliveryadmin.R.string.action_retry,
        iconId = com.bunbeauty.fooddeliveryadmin.R.drawable.ic_repeat,
        extraTextId = com.bunbeauty.fooddeliveryadmin.R.string.action_retry,
        action = {},
    )
}
