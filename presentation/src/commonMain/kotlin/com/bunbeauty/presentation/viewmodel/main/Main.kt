package com.bunbeauty.presentation.viewmodel.main

import androidx.navigation.NavController
import com.bunbeauty.presentation.viewmodel.base.BaseAction
import com.bunbeauty.presentation.viewmodel.base.BaseEvent
import com.bunbeauty.presentation.viewmodel.base.BaseViewDataState

interface Main {
    data class ViewDataState(
        val connectionLost: Boolean,
        val nonWorkingDay: Boolean,
        val navigationBarOptions: NavigationBarOptions,
    ) : BaseViewDataState

    enum class NavigationBarItem {
        ORDERS,
        MENU,
        PROFILE,
    }

    sealed interface NavigationBarOptions {
        data object Hidden : NavigationBarOptions

        data class Visible(
            val selectedItem: NavigationBarItem,
            val navController: NavController,
        ) : NavigationBarOptions
    }

    sealed interface Action : BaseAction {
        data class UpdateNavDestination(
            val navigationBarItem: NavigationBarItem?,
            val navController: NavController,
        ) : Action

        data class ShowErrorMessage(
            val text: String,
        ) : Action

        data class ShowInfoMessage(
            val text: String,
        ) : Action
    }

    sealed interface Event : BaseEvent {
        class ShowMessageEvent(
            val message: Message,
        ) : Event
    }

    data class Message(
        val type: Type,
        val text: String,
    ) {
        enum class Type {
            INFO,
            ERROR,
        }
    }
}
