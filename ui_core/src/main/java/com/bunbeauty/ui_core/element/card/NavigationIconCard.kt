package com.bunbeauty.ui_core.element.card

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import com.bunbeauty.ui_core.*
import com.bunbeauty.ui_core.element.OverflowingText

@Composable
fun NavigationIconCard(
    modifier: Modifier = Modifier,
    @DrawableRes iconId: Int,
    @StringRes iconDescription: Int,
    @StringRes labelStringId: Int? = null,
    label: String = "",
    hasShadow: Boolean = true,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .card(hasShadow)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(),
                onClick = onClick
            ),
        backgroundColor = FoodDeliveryTheme.colors.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(FoodDeliveryTheme.dimensions.mediumSpace),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.icon(),
                imageVector = ImageVector.vectorResource(iconId),
                contentDescription = stringResource(iconDescription),
                tint = FoodDeliveryTheme.colors.onSurfaceVariant
            )
            val labelText = labelStringId?.let { id ->
                stringResource(id)
            } ?: label
            OverflowingText(
                modifier = Modifier
                    .padding(horizontal = FoodDeliveryTheme.dimensions.mediumSpace)
                    .weight(1f),
                text = labelText,
                style = FoodDeliveryTheme.typography.body1,
                color = FoodDeliveryTheme.colors.onSurface
            )
            Icon(
                modifier = Modifier.smallIcon(),
                imageVector = ImageVector.vectorResource(R.drawable.ic_right_arrow),
                contentDescription = stringResource(R.string.description_ic_next),
                tint = FoodDeliveryTheme.colors.onSurfaceVariant
            )
        }
    }
}

@ExperimentalMaterialApi
@Preview
@Composable
fun NavigationIconCardPreview() {
    NavigationIconCard(
        iconId = R.drawable.ic_info,
        iconDescription = R.string.msg_example_text,
        label = "Текст"
    ) {}
}
