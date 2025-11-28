package com.bunbeauty.domain.model.menuProcutToAdditionGroupToAddition

data class MenuProductToAdditionGroupToAddition(
    val uuid: String,
    val isSelected: Boolean,
    val isVisible: Boolean,
    val menuProductToAdditionGroupUuid: String,
    val additionUuid: String,
    val priority: Int?
)
