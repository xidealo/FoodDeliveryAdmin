package com.bunbeauty.fooddeliveryadmin.screen.menulist.common

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable

@Immutable
data class CardFieldUi(
    @StringRes val hintResId: Int,
    override val value: String,
    override val isError: Boolean,
    @StringRes val errorResId: Int
) : FieldUi<String>() {
    val errorResIdToShow: Int? = errorResId.takeIf { isError }
}