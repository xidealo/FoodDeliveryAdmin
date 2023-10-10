package com.bunbeauty.presentation.viewmodel.main

import androidx.navigation.NavController
import com.bunbeauty.presentation.viewmodel.base.Action

sealed interface MainAction : Action {
    data class UpdateNavDestination(
        val navigationBarItem: AdminNavigationBarItem?,
        val navController: NavController,
    ) : MainAction

    data class ShowErrorMessage(val text: String) : MainAction
    data class ShowInfoMessage(val text: String) : MainAction
}