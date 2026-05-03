package com.bunbeauty.shared.designsystem.compose.element.selectableitem

import androidx.compose.runtime.Immutable

@Immutable
data class SelectableItem(
    val uuid: String,
    val title: String,
    val isSelected: Boolean,
)
