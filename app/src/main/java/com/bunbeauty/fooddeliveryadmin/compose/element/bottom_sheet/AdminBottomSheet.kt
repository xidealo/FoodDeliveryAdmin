package com.bunbeauty.fooddeliveryadmin.compose.element.bottom_sheet

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme
import com.bunbeauty.fooddeliveryadmin.compose.theme.bold

@Composable
fun AdminBottomSheet(
    @StringRes titleStringId: Int,
    content: @Composable ColumnScope.() -> Unit
) {
    AdminBottomSheet(
        title = stringResource(id = titleStringId),
        content = content
    )
}

@Composable
fun AdminBottomSheet(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    AdminBottomSheet(
        title = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = title,
                style = AdminTheme.typography.titleMedium.bold,
                color = AdminTheme.colors.main.onSurface,
                textAlign = TextAlign.Center
            )
        },
        content = content
    )
}

@Composable
private fun AdminBottomSheet(
    title: @Composable () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(AdminBottomSheetDefaults.bottomSheetShape)
            .background(AdminTheme.colors.main.surface)
            .padding(horizontal = AdminTheme.dimensions.screenContentSpace)
            .padding(bottom = AdminTheme.dimensions.screenContentSpace, top = 8.dp)
    ) {
        Spacer(
            modifier = Modifier
                .width(32.dp)
                .height(4.dp)
                .background(
                    color = AdminTheme.colors.main.onSurfaceVariant,
                    shape = RoundedCornerShape(2.dp)
                )
                .align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(16.dp))
        title()
        Spacer(modifier = Modifier.height(16.dp))
        content()
    }
}
