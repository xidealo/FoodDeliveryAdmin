package com.bunbeauty.presentation.viewmodel.main

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.feature.main.GetIsNonWorkingDayFlowUseCase
import com.bunbeauty.presentation.extension.launchSafe
import com.bunbeauty.presentation.viewmodel.base.BaseStateViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.asSharedFlow

class MainViewModel(
    getIsNonWorkingDayFlow: GetIsNonWorkingDayFlowUseCase,
) : BaseStateViewModel<Main.ViewDataState, Main.Action, Main.Event>(
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

    init {
        viewModelScope.launchSafe(
            block = {
                getIsNonWorkingDayFlow()
                    .onEach { isNonWorkingDay ->
                        setState {
                            copy(nonWorkingDay = isNonWorkingDay)
                        }
                    }.launchIn(viewModelScope)
            },
            onError = {
                // todo log error
            },
        )
    }

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
                showMessage(action.text, Main.Message.Type.INFO)
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
    ) {
        mutableSnackbarMessages.tryEmit(
            Main.Message(
                type = type,
                text = text,
            ),
        )
    }
}
