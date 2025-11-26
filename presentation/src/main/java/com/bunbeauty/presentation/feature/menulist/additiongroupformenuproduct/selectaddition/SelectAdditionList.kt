package com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct.selectaddition

import com.bunbeauty.presentation.viewmodel.base.BaseAction
import com.bunbeauty.presentation.viewmodel.base.BaseEvent
import com.bunbeauty.presentation.viewmodel.base.BaseViewDataState

interface SelectAdditionList {
    data class DataState(
        val state: State,
        val selectedAdditionList: List<AdditionItem>,
        val notSelectedAdditionList: List<AdditionItem>,
        val groupName: String,
        val emptySelectedList: Boolean,
        val editedAdditionListUuid: List<String>?,
    ) : BaseViewDataState {
        data class AdditionItem(
            val uuid: String,
            val name: String,
        )

        enum class State {
            LOADING,
            ERROR,
            SUCCESS,
            SUCCESS_DRAG_DROP,
        }
    }

    sealed interface Action : BaseAction {
        data class Init(
            val menuProductUuid: String,
            val additionGroupUuid: String?,
            val additionGroupName: String,
            val editedAdditionListUuid: List<String>?,
        ) : Action

        data object OnBackClick : Action

        data class SelectAdditionClick(
            val uuid: String,
        ) : Action

        data class RemoveAdditionClick(
            val uuid: String,
        ) : Action

        data object SelectAdditionListClick : Action

        data class MoveSelectedItem(
            val fromIndex: Int,
            val toIndex: Int,
        ) : Action

        data object OnPriorityEditClicked : Action

        data object OnCancelClicked : Action

        data object OnSaveEditPriorityClick : Action
    }

    sealed interface Event : BaseEvent {
        data object Back : Event

        data class SelectAdditionListBack(
            val additionUuidList: List<String>,
        ) : Event

        data object ShowUpdateSelectAdditionListSuccess : Event
    }
}
