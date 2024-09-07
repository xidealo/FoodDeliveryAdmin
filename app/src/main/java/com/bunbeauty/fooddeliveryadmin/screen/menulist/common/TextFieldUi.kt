package com.bunbeauty.fooddeliveryadmin.screen.menulist.common

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable

@Immutable
data class TextFieldUi(
    override val value: String,
    override val isError: Boolean,
    @StringRes val errorResId: Int
) : FieldUi<String>()
