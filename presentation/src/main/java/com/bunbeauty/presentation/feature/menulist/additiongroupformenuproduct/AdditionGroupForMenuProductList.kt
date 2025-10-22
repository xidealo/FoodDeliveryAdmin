package com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct

import com.bunbeauty.presentation.viewmodel.base.BaseAction
import com.bunbeauty.presentation.viewmodel.base.BaseDataState
import com.bunbeauty.presentation.viewmodel.base.BaseEvent

interface AdditionGroupForMenuProductList {
    data class DataState(
        val additionGroupList: List<AdditionGroupForMenuProduct>,
        val state: State
    ) : BaseDataState {
        data class AdditionGroupForMenuProduct(
            val uuid: String,
            val name: String,
            val additionNameList: String?
        )

        enum class State {
            LOADING,
            SUCCESS,
            ERROR
        }
    }

    sealed interface Action : BaseAction {
        data class Init(val menuProductUuid: String) : Action
        data object OnBackClick : Action
        data class OnAdditionGroupClick(val uuid: String) : Action
    }

    sealed interface Event : BaseEvent {
        data object Back : Event
        data class OnAdditionGroupClick(
            val additionGroupUuid: String
        ) : Event
    }
}
