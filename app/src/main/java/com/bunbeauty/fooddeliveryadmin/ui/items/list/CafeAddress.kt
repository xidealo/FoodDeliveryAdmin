package com.bunbeauty.fooddeliveryadmin.ui.items.list

import kotlinx.parcelize.Parcelize

@Parcelize
data class CafeAddress(
    override val title: String,
    val cafeUuid: String
): ListModel()