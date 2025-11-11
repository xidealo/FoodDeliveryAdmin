package com.bunbeauty.fooddeliveryadmin.screen.menulist.additiongroupformenuproduct

import com.bunbeauty.domain.model.additiongroup.AdditionGroupForMenuProduct
import com.bunbeauty.presentation.viewmodel.base.BaseViewState

data class AdditionGroupForMenuProductListViewState(
    val state: State,
    val isRefreshing: Boolean,
    val isEditPriority: Boolean,
) : BaseViewState {
    sealed interface State {
        data object Loading : State

        data object Error : State

        data class Success(
            val additionGroupWithAdditionsList: List<AdditionGroupForMenuProduct>,
        ) : State

        data class SuccessDragDrop(
            val additionGroupWithAdditionsList: List<AdditionGroupForMenuProduct>,
        ) : State
    }
}
