package com.bunbeauty.fooddeliveryadmin.compose.element.bottomsheet

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme

@Composable
fun ColumnScope.AdminBottomSheetContent(
    @StringRes titleResId: Int,
    content: @Composable () -> Unit,
) {
    Text(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(
                    top = 16.dp,
                    bottom = 12.dp,
                ),
        text = stringResource(titleResId),
        color = AdminTheme.colors.main.onSurface,
        style = AdminTheme.typography.titleMedium,
        textAlign = TextAlign.Center,
    )
    Divider(
        modifier = Modifier.fillMaxWidth(),
        thickness = 0.5.dp,
        color = AdminTheme.colors.main.onSurfaceVariant,
    )
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        content()
    }
}
