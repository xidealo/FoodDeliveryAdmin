package com.bunbeauty.presentation.model

data class MenuState(
    val menuProductItems: List<MenuProductItem> = listOf(),
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val throwable: Throwable? = null
) {

    data class MenuProductItem(
        val uuid: String,
        val name: String,
        val photoLink: String,
        val visible: Boolean,
        val newCost: String,
        val oldCost: String,
    )

}
