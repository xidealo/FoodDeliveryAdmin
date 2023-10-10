package com.bunbeauty.fooddeliveryadmin.compose.element.button

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme

object AdminButtonDefaults {

    private val buttonElevation: ButtonElevation
        @Composable get() = ButtonDefaults.buttonElevation(2.dp)

    private val zeroButtonElevation: ButtonElevation
        @Composable get() = ButtonDefaults.buttonElevation(0.dp)

    val mainButtonColors: ButtonColors
        @Composable get() = ButtonDefaults.buttonColors(
            containerColor = AdminTheme.colors.main.primary,
            contentColor = AdminTheme.colors.main.onPrimary,
            disabledContainerColor = AdminTheme.colors.main.disabled,
            disabledContentColor = AdminTheme.colors.main.onDisabled
        )

    val negativeButtonColors: ButtonColors
        @Composable get() = ButtonDefaults.buttonColors(
            containerColor = AdminTheme.colors.status.negative,
            contentColor = AdminTheme.colors.status.onStatus,
            disabledContainerColor = AdminTheme.colors.main.disabled,
            disabledContentColor = AdminTheme.colors.main.onDisabled
        )

    val secondaryButtonColors: ButtonColors
        @Composable get() = ButtonDefaults.buttonColors(
            containerColor = AdminTheme.colors.main.secondary,
            contentColor = AdminTheme.colors.main.onSecondary,
            disabledContainerColor = AdminTheme.colors.main.disabled,
            disabledContentColor = AdminTheme.colors.main.onDisabled
        )

    val buttonShape: RoundedCornerShape
        @Composable get() = RoundedCornerShape(AdminTheme.dimensions.buttonRadius)

    @Composable
    fun getButtonElevation(elevated: Boolean): ButtonElevation = if (elevated) {
        buttonElevation
    } else {
        zeroButtonElevation
    }
}
