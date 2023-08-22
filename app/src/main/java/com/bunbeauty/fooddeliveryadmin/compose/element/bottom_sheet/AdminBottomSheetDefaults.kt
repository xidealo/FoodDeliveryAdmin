package com.bunbeauty.fooddeliveryadmin.compose.element.bottom_sheet

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme

object AdminBottomSheetDefaults {

    val bottomSheetShape: RoundedCornerShape
        @Composable get() = RoundedCornerShape(
            topStart = AdminTheme.dimensions.bottomSheetRadius,
            topEnd = AdminTheme.dimensions.bottomSheetRadius,
            bottomEnd = 0.dp,
            bottomStart = 0.dp
        )
}
