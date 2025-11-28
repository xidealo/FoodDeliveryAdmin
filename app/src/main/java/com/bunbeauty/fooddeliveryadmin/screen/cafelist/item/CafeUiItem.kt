package com.bunbeauty.fooddeliveryadmin.screen.cafelist.item

import com.bunbeauty.domain.model.cafe.CafeStatus

data class CafeUiItem(
    val uuid: String,
    val address: String,
    val workingHours: String,
    val cafeStatusText: String,
    val cafeStatus: CafeStatus
)
