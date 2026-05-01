package com.bunbeauty.shared.designsystem.compose

import androidx.compose.runtime.Immutable
import org.jetbrains.compose.resources.StringResource

@Immutable
data class CardFieldUi(
    val labelResId: String,
    val value: String?,
    val isError: Boolean,
    val errorResId: StringResource,
)
