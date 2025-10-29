package com.bunbeauty.domain.model.menuproduct

data class MenuProductPost(
    val name: String,
    val newPrice: Int,
    val oldPrice: Int?,
    val utils: String?,
    val nutrition: Int?,
    val description: String,
    val comboDescription: String?,
    val photoLink: String,
    val barcode: Int,
    val isVisible: Boolean,
    val isRecommended: Boolean,
    val categories: List<String>
) {
    companion object {
        val mock = MenuProductPost(
            name = "",
            newPrice = 0,
            oldPrice = null,
            utils = null,
            nutrition = null,
            description = "",
            comboDescription = null,
            photoLink = "",
            barcode = 0,
            isVisible = false,
            isRecommended = false,
            categories = emptyList()
        )
    }
}
