package com.bunbeauty.shared.viewmodel.main

import androidx.compose.ui.unit.Dp
import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.feature.main.GetIsNonWorkingDayFlowUseCase
import com.bunbeauty.shared.extension.launchSafe
import com.bunbeauty.shared.viewmodel.base.BaseStateViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainViewModel : BaseStateViewModel<Main.ViewDataState, Main.Action, Main.Event>(
    initState =
        Main.ViewDataState(
            connectionLost = false,
            nonWorkingDay = false,
            navigationBarOptions = Main.NavigationBarOptions.Hidden,
        ),
) {
    private val mutableSnackbarMessages =
        MutableSharedFlow<Main.Message>(
            replay = 0,
            extraBufferCapacity = 1,
            onBufferOverflow = BufferOverflow.DROP_OLDEST,
        )
    val snackbarMessages = mutableSnackbarMessages.asSharedFlow()

    override fun reduce(
        action: Main.Action,
        dataState: Main.ViewDataState,
    ) {
        when (action) {
            is Main.Action.UpdateNavDestination -> {
                updateNavDestination(
                    navigationBarItem = action.navigationBarItem,
                )
            }

            is Main.Action.ShowInfoMessage -> {
                showMessage(
                    text = action.text,
                    type = Main.Message.Type.INFO,
                    paddingBottom = action.paddingBottom,
                )
            }

            is Main.Action.ShowErrorMessage -> {
                showMessage(action.text, Main.Message.Type.ERROR)
            }
        }
    }

    private fun updateNavDestination(navigationBarItem: Main.NavigationBarItem?) {
        val navigationBarOptions =
            navigationBarItem?.let {
                Main.NavigationBarOptions.Visible(
                    selectedItem = navigationBarItem,
                )
            } ?: Main.NavigationBarOptions.Hidden

        setState {
            copy(navigationBarOptions = navigationBarOptions)
        }
    }

    private fun showMessage(
        text: String,
        type: Main.Message.Type,
        paddingBottom: Dp? = null,
    ) {
        mutableSnackbarMessages.tryEmit(
            Main.Message(
                type = type,
                text = text,
                paddingBottom = paddingBottom,
            ),
        )
    }
}
