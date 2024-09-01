package com.bunbeauty.presentation.feature.menulist.common

data class TextFieldData(
    override val value: String,
    override val isError: Boolean,
): FieldData<String>()