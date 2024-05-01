package com.bunbeauty.presentation.feature.menu

import com.bunbeauty.presentation.viewmodel.base.BaseStateViewModel

class MenuViewModel : BaseStateViewModel<Menu.ViewDataState, Menu.Action, Menu.Event>(
    initState = Menu.ViewDataState
) {

    override fun reduce(action: Menu.Action, dataState: Menu.ViewDataState) {
        when (action) {
            Menu.Action.OnAdditionGroupsListClick -> addEvent {
                Menu.Event.OnAdditionGroupsListClick
            }

            Menu.Action.OnAdditionsListClick -> addEvent {
                Menu.Event.OnAdditionsListClick
            }

            Menu.Action.OnMenuListClick -> addEvent {
                Menu.Event.OnMenuListClick
            }
        }
    }
}
