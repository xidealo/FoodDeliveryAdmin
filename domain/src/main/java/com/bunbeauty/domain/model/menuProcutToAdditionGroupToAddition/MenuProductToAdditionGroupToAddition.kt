package com.bunbeauty.domain.model.menuProcutToAdditionGroupToAddition

data class MenuProductToAdditionGroupToAddition(
    val uuid: String,
    val isSelected: Boolean,
    val isVisible: Boolean,
    val menuProductToAdditionGroupUuid: String,
    val additionUuid: String,
    val priority: Int?,
) {
    companion object {
        val mock =
            MenuProductToAdditionGroupToAddition(
                uuid = "",
                isSelected = false,
                isVisible = false,
                menuProductToAdditionGroupUuid = "",
                additionUuid = "",
                priority = null,
            )
    }
}
