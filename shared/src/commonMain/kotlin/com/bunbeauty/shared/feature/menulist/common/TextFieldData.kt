package com.bunbeauty.shared.feature.menulist.common

data class TextFieldData(
    override val value: String,
    override val isError: Boolean,
) : FieldData<String>() {
    companion object {
        val empty =
            TextFieldData(
                value = "",
                isError = false,
            )
    }
}
