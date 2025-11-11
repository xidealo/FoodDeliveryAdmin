package com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct

import com.bunbeauty.domain.model.additiongroup.AdditionGroupForMenuProduct
import com.bunbeauty.presentation.viewmodel.base.BaseAction
import com.bunbeauty.presentation.viewmodel.base.BaseDataState
import com.bunbeauty.presentation.viewmodel.base.BaseEvent

interface AdditionGroupForMenuProductList {
    data class DataState(
        val additionGroupList: List<AdditionGroupForMenuProduct>,
        val state: State,
        val isRefreshing: Boolean,
        val isEditPriority: Boolean,
    ) : BaseDataState {
        enum class State {
            LOADING,
            SUCCESS,
            ERROR,
            SUCCESS_DRAG_DROP,
        }
    }

    sealed interface Action : BaseAction {
        data class Init(
            val menuProductUuid: String,
        ) : Action

        data class RefreshData(
            val menuProductUuid: String,
        ) : Action

        data object OnBackClick : Action

        data class OnAdditionGroupClick(
            val uuid: String,
        ) : Action

        data object OnCreateClick : Action

        data class MoveSelectedItem(
            val fromIndex: Int,
            val toIndex: Int,
        ) : Action

        data object OnPriorityEditClicked : Action

        data object OnCancelClicked : Action

        data class OnSaveEditPriorityClick(
            val updateAdditionGroupForMenuProductList: List<AdditionGroupForMenuProduct>,
        ) : Action
    }

    sealed interface Event : BaseEvent {
        data object Back : Event

        data object OnCreateClicked : Event

        data class OnAdditionGroupClicked(
            val additionGroupUuid: String,
        ) : Event
    }
}
