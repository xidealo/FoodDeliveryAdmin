package com.bunbeauty.fooddeliveryadmin.compose.element.button

import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.runtime.Composable

@Composable
fun RadioButton(
    selected: Boolean,
    colors: RadioButtonColors = RadioButtonDefaults.radioButtonColors,
    onClick: () -> Unit
) {
    RadioButton(
        selected = selected,
        onClick = onClick,
        colors = colors
    )
}