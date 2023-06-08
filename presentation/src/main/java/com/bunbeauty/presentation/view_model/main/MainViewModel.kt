package com.bunbeauty.presentation.view_model.main

import androidx.navigation.NavController
import com.bunbeauty.presentation.view_model.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : BaseViewModel() {

    private val mutableMainUiState = MutableStateFlow(MainUiState())
    val mainUiState = mutableMainUiState.asStateFlow()

    fun onNavDestinationUpdated(
        navigationBarItem: AdminNavigationBarItem?,
        navController: NavController
    ) {
        val navigationBarOptions = navigationBarItem?.let {
            NavigationBarOptions.Visible(
                selectedItem = navigationBarItem,
                navController = navController
            )
        } ?: NavigationBarOptions.Hidden

        mutableMainUiState.update { state ->
            state.copy(navigationBarOptions = navigationBarOptions)
        }
    }

    fun showInfoMessage(text: String) {
        showMessage(text, AdminMessageType.INFO)
    }

    fun showErrorMessage(text: String) {
        showMessage(text, AdminMessageType.ERROR)
    }

    fun consumeEventList(eventList: List<MainUiState.Event>) {
        mutableMainUiState.update { state ->
            state - eventList
        }
    }

    private fun showMessage(text: String, type: AdminMessageType) {
        mutableMainUiState.update { state ->
            state + MainUiState.Event.ShowMessageEvent(
                message = AdminMessage(
                    type = type,
                    text = text
                )
            )
        }
    }
}