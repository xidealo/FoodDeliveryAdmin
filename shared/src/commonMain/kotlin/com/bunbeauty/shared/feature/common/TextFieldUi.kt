package com.bunbeauty.shared.feature.common

import androidx.compose.runtime.Immutable
import org.jetbrains.compose.resources.StringResource

@Immutable
data class TextFieldUi(
    override val value: String,
    override val isError: Boolean,
    val errorRes: StringResource?,
) : FieldUi<String>() {
    companion object {
        val empty =
            TextFieldUi(
                value = "",
                isError = false,
                errorRes = null,
            )
    }
}
