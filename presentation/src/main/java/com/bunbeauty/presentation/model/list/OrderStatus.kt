package com.bunbeauty.presentation.model.list

import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderStatus(
    override val title: String
): ListItemModel()