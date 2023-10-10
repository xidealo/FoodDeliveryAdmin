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
) : BaseStateViewModel<MainState, MainAction, MainEvent>(
    initState = MainState(
        connectionLost = false,
        nonWorkingDay = false,
        navigationBarOptions = NavigationBarOptions.Hidden,
    )
) {

    init {
        getIsNonWorkingDayFlow().onEach { isNonWorkingDay ->
            state { state ->
                state.copy(nonWorkingDay = isNonWorkingDay)
            }
        }.launchIn(viewModelScope)
    }

    override fun handleAction(action: MainAction) {
        when (action) {
            is MainAction.UpdateNavDestination -> {
                updateNavDestination(
                    navigationBarItem = action.navigationBarItem,
                    navController = action.navController,
                )
            }

            is MainAction.ShowInfoMessage -> {
                showMessage(action.text, AdminMessageType.INFO)
            }

            is MainAction.ShowErrorMessage -> {
                showMessage(action.text, AdminMessageType.ERROR)
            }
        }
    }

    private fun updateNavDestination(
        navigationBarItem: AdminNavigationBarItem?,
        navController: NavController,
    ) {
        val navigationBarOptions = navigationBarItem?.let {
            NavigationBarOptions.Visible(
                selectedItem = navigationBarItem,
                navController = navController
            )
        } ?: NavigationBarOptions.Hidden

        state { state ->
            state.copy(navigationBarOptions = navigationBarOptions)
        }
    }

    private fun showMessage(text: String, type: AdminMessageType) {
        event {
            MainEvent.ShowMessageEvent(
                message = AdminMessage(
                    type = type,
                    text = text
                )
            )
        }
    }

}