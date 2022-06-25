package com.bunbeauty.ui_core.element.card

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.bunbeauty.ui_core.FoodDeliveryTheme
import com.bunbeauty.ui_core.R
import com.bunbeauty.ui_core.card

@Composable
fun TextCard(
    modifier: Modifier = Modifier,
    @StringRes hintStringId: Int,
    label: String
) {
    Card(
        modifier = modifier.card(true),
        backgroundColor = FoodDeliveryTheme.colors.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(FoodDeliveryTheme.dimensions.mediumSpace)
        ) {
            Text(
                text = stringResource(hintStringId),
                style = FoodDeliveryTheme.typography.body2,
                color = FoodDeliveryTheme.colors.onSurfaceVariant
            )
            Text(
                modifier = Modifier.padding(top = FoodDeliveryTheme.dimensions.verySmallSpace),
                text = label,
                style = FoodDeliveryTheme.typography.body1,
                color = FoodDeliveryTheme.colors.onSurface
            )
        }
    }
}

@Preview
@Composable
fun TextCardPreview() {
    TextCard(
        hintStringId = R.string.msg_example_text,
        label = "+7 999 000-00-00"
    )
}
