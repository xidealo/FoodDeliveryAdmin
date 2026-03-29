package com.bunbeauty.presentation.viewmodel.main

import androidx.compose.ui.unit.Dp
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
        ) : NavigationBarOptions
    }

    sealed interface Action : BaseAction {
        data class UpdateNavDestination(
            val navigationBarItem: NavigationBarItem?,
        ) : Action

        data class ShowErrorMessage(
            val text: String,
        ) : Action

        data class ShowInfoMessage(
            val text: String,
            val paddingBottom: Dp,
        ) : Action
    }

    sealed interface Event : BaseEvent

    data class Message(
        val type: Type,
        val text: String,
        val paddingBottom: Dp? = null,
    ) {
        enum class Type {
            INFO,
            ERROR,
        }
    }
}
