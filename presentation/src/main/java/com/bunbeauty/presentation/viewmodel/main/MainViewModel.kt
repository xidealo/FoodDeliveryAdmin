package com.bunbeauty.presentation.viewmodel.main

import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.bunbeauty.domain.feature.main.GetIsNonWorkingDayFlowUseCase
import com.bunbeauty.presentation.viewmodel.base.BaseStateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    getIsNonWorkingDayFlow: GetIsNonWorkingDayFlowUseCase,
) : BaseStateViewModel<Main.ViewDataState, Main.Action, Main.Event>(
    initState = Main.ViewDataState(
        connectionLost = false,
        nonWorkingDay = false,
        navigationBarOptions = Main.NavigationBarOptions.Hidden,
    )
) {

    init {
        getIsNonWorkingDayFlow().onEach { isNonWorkingDay ->
            setState {
                copy(nonWorkingDay = isNonWorkingDay)
            }
        }.launchIn(viewModelScope)
    }

    override fun reduce(action: Main.Action, dataState: Main.ViewDataState) {
        when (action) {
            is Main.Action.UpdateNavDestination -> {
                updateNavDestination(
                    navigationBarItem = action.navigationBarItem,
                    navController = action.navController,
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

    private fun updateNavDestination(
        navigationBarItem: Main.NavigationBarItem?,
        navController: NavController,
    ) {
        val navigationBarOptions = navigationBarItem?.let {
            Main.NavigationBarOptions.Visible(
                selectedItem = navigationBarItem,
                navController = navController
            )
        } ?: Main.NavigationBarOptions.Hidden

        setState {
            copy(navigationBarOptions = navigationBarOptions)
        }
    }

    private fun showMessage(text: String, type: Main.Message.Type) {
        addEvent {
            Main.Event.ShowMessageEvent(
                message = Main.Message(
                    type = type,
                    text = text
                )
            )
        }
    }

}