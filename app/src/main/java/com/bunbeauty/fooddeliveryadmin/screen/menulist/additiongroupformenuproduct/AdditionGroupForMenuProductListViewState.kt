package com.bunbeauty.fooddeliveryadmin.screen.menulist.additiongroupformenuproduct

import com.bunbeauty.presentation.viewmodel.base.BaseViewState

data class AdditionGroupForMenuProductListViewState(
    val state: State
) : BaseViewState {
    data class AdditionGroupWithAdditions(
        val uuid: String,
        val name: String,
        val additionNameList: String?
    )

    sealed interface State {
        data object Loading : State
        data object Error : State
        data class Success(
            val additionGroupWithAdditionsList: List<AdditionGroupWithAdditions>
        ) : State
    }
}
