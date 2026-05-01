package com.bunbeauty.shared.designsystem.compose

import androidx.compose.runtime.Immutable
import org.jetbrains.compose.resources.StringResource

@Immutable
data class TextFieldUi(
    val value: String,
    val isError: Boolean,
    val errorResId: StringResource?,
)
