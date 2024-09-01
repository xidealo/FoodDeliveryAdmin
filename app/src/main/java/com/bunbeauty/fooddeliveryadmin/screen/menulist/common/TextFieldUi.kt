package com.bunbeauty.fooddeliveryadmin.screen.menulist.common

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable

@Immutable
data class TextFieldUi(
    override val value: String,
    override val isError: Boolean,
     @StringRes private val errorResId: Int
): FieldUi<String>() {
    val errorResIdToShow: Int? = errorResId.takeIf { isError }
}