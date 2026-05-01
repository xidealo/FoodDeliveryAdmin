package com.bunbeauty.shared.feature.common

import androidx.compose.runtime.Immutable

@Immutable
data class CardFieldUi(
    val labelResId: Int,
    override val value: String?,
    override val isError: Boolean,
    val errorResId: Int,
) : FieldUi<String?>()
