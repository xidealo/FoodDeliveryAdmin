package com.bunbeauty.domain.model.menuproduct

import com.bunbeauty.domain.model.additiongroup.AdditionGroupWithAdditions

data class MenuProduct(
    val uuid: String,
    val name: String,
    val newPrice: Int,
    val oldPrice: Int?,
    val units: String?,
    val nutrition: Int?,
    val description: String,
    val comboDescription: String?,
    val photoLink: String,
    val barcode: Int?,
    val isVisible: Boolean,
    val isRecommended: Boolean,
    val categoryUuids: List<String>,
    val additionGroups: List<AdditionGroupWithAdditions>
)
