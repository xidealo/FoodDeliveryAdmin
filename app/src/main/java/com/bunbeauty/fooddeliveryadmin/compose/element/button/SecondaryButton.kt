package com.bunbeauty.fooddeliveryadmin.compose.element.button

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
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
    onClick: () -> Unit,
    elevated: Boolean = true,
    isEnabled: Boolean = true,
    @DrawableRes icon: Int? = null,
    borderColor: Color? = null,
    buttonColors: ButtonColors? = null
) {
    CompositionLocalProvider(
        LocalMinimumInteractiveComponentEnforcement provides false
    ) {
        OutlinedButton(
            modifier = modifier.fillMaxWidth(),
            onClick = onClick,
            colors = buttonColors ?: AdminButtonDefaults.secondaryButtonColors,
            border = BorderStroke(
                width = 2.dp,
                color = borderColor ?: AdminTheme.colors.main.onSecondary
            ),
            shape = AdminButtonDefaults.buttonShape,
            elevation = AdminButtonDefaults.getButtonElevation(elevated),
            enabled = isEnabled
        ) {
            icon?.let { icon ->
                Icon(
                    modifier = Modifier
                        .padding(end = 8.dp),
                    painter = painterResource(icon),
                    contentDescription = null
                )
            }
            Text(
                text = stringResource(textStringId),
                style = AdminTheme.typography.labelLarge.medium
            )
        }
    }
}

@Preview
@Composable
private fun SecondaryButtonPreview() {
    AdminTheme {
        SecondaryButton(
            textStringId = R.string.action_common_cancel,
            onClick = { }
        )
    }
}

@Preview
@Composable
private fun SecondaryButtonWithIconPreview() {
    AdminTheme {
        SecondaryButton(
            textStringId = R.string.action_common_cancel,
            onClick = { },
            icon = R.drawable.ic_add_photo,
            borderColor = AdminTheme.colors.main.primary,
            buttonColors = AdminButtonDefaults.secondaryPrimaryButtonColors
        )
    }
}
