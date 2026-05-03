package com.bunbeauty.shared.designsystem.compose.element.button

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bunbeauty.shared.designsystem.compose.theme.AdminTheme
import com.bunbeauty.shared.designsystem.compose.theme.medium
import fooddeliveryadmin.shared.generated.resources.Res
import fooddeliveryadmin.shared.generated.resources.action_edit_cafe_add
import fooddeliveryadmin.shared.generated.resources.ic_plus
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FloatingButton(
    modifier: Modifier = Modifier,
    iconId: DrawableResource,
    textStringId: StringResource? = null,
    text: String? = null,
    onClick: () -> Unit,
) {
    CompositionLocalProvider(
        LocalMinimumInteractiveComponentSize provides 0.dp,
    ) {
        FloatingActionButton(
            modifier = modifier.height(40.dp),
            onClick = onClick,
            shape = AdminButtonDefaults.buttonShape,
            containerColor = AdminTheme.colors.main.primary,
            contentColor = AdminTheme.colors.main.onPrimary,
            elevation = FloatingActionButtonDefaults.elevation(2.dp),
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    modifier = Modifier.size(16.dp),
                    painter = painterResource(iconId),
                    contentDescription = null,
                )

                val buttonText =
                    text ?: textStringId?.let {
                        stringResource(it)
                    } ?: ""
                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = buttonText,
                    style = AdminTheme.typography.labelLarge.medium,
                )
            }
        }
    }
}

@Preview
@Composable
private fun FloatingButtonPreview() {
    AdminTheme {
        FloatingButton(
            iconId = Res.drawable.ic_plus,
            textStringId = Res.string.action_edit_cafe_add,
        ) {}
    }
}
