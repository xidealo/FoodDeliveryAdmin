package com.bunbeauty.domain.model.menuproduct

data class UpdateMenuProduct(
    val name: String? = null,
    val newPrice: Int? = null,
    val oldPrice: Int? = null,
    val utils: String? = null,
    val nutrition: Int? = null,
    val description: String? = null,
    val comboDescription: String? = null,
    val photoLink: String? = null,
    val isVisible: Boolean? = null,
    val isRecommended: Boolean? = null,
    val categories: List<String>? = null
)
