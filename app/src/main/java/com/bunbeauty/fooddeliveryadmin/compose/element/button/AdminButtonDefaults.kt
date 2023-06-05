package com.bunbeauty.fooddeliveryadmin.compose.element.button

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
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
            containerColor = AdminTheme.colors.mainColors.primary,
            contentColor = AdminTheme.colors.mainColors.onPrimary,
            disabledContainerColor = AdminTheme.colors.mainColors.disabled,
            disabledContentColor = AdminTheme.colors.mainColors.onDisabled
        )

    val secondaryButtonColors: ButtonColors
        @Composable get() = ButtonDefaults.buttonColors(
            containerColor = AdminTheme.colors.mainColors.secondary,
            contentColor = AdminTheme.colors.mainColors.onSecondary,
            disabledContainerColor = AdminTheme.colors.mainColors.disabled,
            disabledContentColor = AdminTheme.colors.mainColors.onDisabled
        )

    val mainOutlineButtonColors: ButtonColors
        @Composable get() = ButtonDefaults.outlinedButtonColors(
            disabledContainerColor = AdminTheme.colors.mainColors.disabled,
            disabledContentColor = AdminTheme.colors.mainColors.onDisabled,
            contentColor = AdminTheme.colors.mainColors.primary
        )

    val iconButtonColors: IconButtonColors
        @Composable get() = IconButtonDefaults.iconButtonColors(
            containerColor = AdminTheme.colors.mainColors.surface,
            contentColor = AdminTheme.colors.mainColors.onSurfaceVariant,
            disabledContainerColor = AdminTheme.colors.mainColors.disabled,
            disabledContentColor = AdminTheme.colors.mainColors.onDisabled
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
