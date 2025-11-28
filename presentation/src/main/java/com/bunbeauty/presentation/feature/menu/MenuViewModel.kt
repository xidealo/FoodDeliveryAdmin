package com.bunbeauty.presentation.feature.menu

import com.bunbeauty.presentation.viewmodel.base.BaseStateViewModel

class MenuViewModel :
    BaseStateViewModel<Menu.DataState, Menu.Action, Menu.Event>(
        initState = Menu.DataState,
    ) {
    override fun reduce(
        action: Menu.Action,
        dataState: Menu.DataState,
    ) {
        when (action) {
            Menu.Action.OnAdditionGroupsListClick ->
                sendEvent {
                    Menu.Event.OnAdditionGroupsListClick
                }

            Menu.Action.OnAdditionsListClick ->
                sendEvent {
                    Menu.Event.OnAdditionsListClick
                }

            Menu.Action.OnMenuListClick ->
                sendEvent {
                    Menu.Event.OnMenuListClick
                }
            Menu.Action.OnCategoriesListClick ->
                sendEvent {
                    Menu.Event.OnCategoriesListClick
                }
        }
    }
}
