package com.bunbeauty.presentation.feature.menu

import com.bunbeauty.presentation.viewmodel.base.BaseAction
import com.bunbeauty.presentation.viewmodel.base.BaseDataState
import com.bunbeauty.presentation.viewmodel.base.BaseEvent

interface Menu {
    data object DataState : BaseDataState

    sealed interface Action : BaseAction {
        data object OnMenuListClick : Action

        data object OnAdditionGroupsListClick : Action

        data object OnAdditionsListClick : Action

        data object OnCategoriesListClick : Action
    }

    sealed interface Event : BaseEvent {
        data object OnMenuListClick : Event

        data object OnAdditionGroupsListClick : Event

        data object OnAdditionsListClick : Event

        data object OnCategoriesListClick : Event
    }
}
