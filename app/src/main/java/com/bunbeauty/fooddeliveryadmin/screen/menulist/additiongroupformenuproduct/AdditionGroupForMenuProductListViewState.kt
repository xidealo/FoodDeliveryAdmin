package com.bunbeauty.fooddeliveryadmin.screen.menulist.additiongroupformenuproduct

import com.bunbeauty.presentation.viewmodel.base.BaseViewState

data class AdditionGroupForMenuProductListViewState(
    val additionGroupWithAdditionsList: List<AdditionGroupWithAdditions>
) : BaseViewState {
    data class AdditionGroupWithAdditions(
        val uuid: String,
        val name: String,
        val additionNameList: String?
    )
}
