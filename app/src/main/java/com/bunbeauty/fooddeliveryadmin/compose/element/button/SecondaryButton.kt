package com.bunbeauty.fooddeliveryadmin.compose.element.button

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme
import com.bunbeauty.fooddeliveryadmin.compose.theme.medium

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecondaryButton(
    modifier: Modifier = Modifier,
    @StringRes textStringId: Int,
    elevated: Boolean = true,
    onClick: () -> Unit,
    isEnabled: Boolean = true,
) {
    CompositionLocalProvider(
        LocalMinimumInteractiveComponentEnforcement provides false,
    ) {
        OutlinedButton(
            modifier = modifier.fillMaxWidth(),
            onClick = onClick,
            colors = AdminButtonDefaults.secondaryButtonColors,
            border = BorderStroke(
                width = 2.dp,
                color = AdminTheme.colors.mainColors.onSecondary
            ),
            shape = AdminButtonDefaults.buttonShape,
            elevation = AdminButtonDefaults.getButtonElevation(elevated),
            enabled = isEnabled
        ) {
            Text(
                text = stringResource(textStringId),
                style = AdminTheme.typography.labelLarge.medium,
            )
        }
    }
}

@Preview()
@Composable
private fun SecondaryButtonPreview() {
    AdminTheme {
        SecondaryButton(
            textStringId = R.string.action_cancel,
            onClick = { }
        )
    }
}
