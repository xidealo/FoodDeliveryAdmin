package com.bunbeauty.presentation.list

import kotlinx.parcelize.Parcelize

@Parcelize
data class CafeAddress(
    override val title: String,
    val cafeUuid: String?
): ListItemModel()