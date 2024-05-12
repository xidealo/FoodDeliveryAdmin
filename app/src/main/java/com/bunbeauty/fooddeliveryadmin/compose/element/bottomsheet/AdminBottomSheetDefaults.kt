package com.bunbeauty.fooddeliveryadmin.compose.element.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme

object AdminBottomSheetDefaults {

    val shape: RoundedCornerShape
        @Composable get() = RoundedCornerShape(
            topStart = AdminTheme.dimensions.bottomSheetRadius,
            topEnd = AdminTheme.dimensions.bottomSheetRadius,
            bottomEnd = 0.dp,
            bottomStart = 0.dp
        )

    @Composable
    fun DragHandle() {
        Spacer(
            modifier = Modifier
                .padding(12.dp)
                .width(36.dp)
                .height(4.dp)
                .background(
                    color = AdminTheme.colors.main.background,
                    shape = RoundedCornerShape(size = 2.5.dp)
                )
        )
    }
}
