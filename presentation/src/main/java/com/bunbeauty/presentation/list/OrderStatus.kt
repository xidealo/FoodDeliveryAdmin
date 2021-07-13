package com.bunbeauty.presentation.list

import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderStatus(
    override val title: String
): ListModel()