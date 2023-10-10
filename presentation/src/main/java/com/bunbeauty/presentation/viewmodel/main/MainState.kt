package com.bunbeauty.presentation.viewmodel.main

import androidx.navigation.NavController
import com.bunbeauty.presentation.viewmodel.base.ViewDataState

data class MainState(
    val connectionLost: Boolean,
    val nonWorkingDay: Boolean,
    val navigationBarOptions: NavigationBarOptions,
): ViewDataState

enum class AdminNavigationBarItem {
    ORDERS,
    MENU,
    PROFILE,
}

sealed interface NavigationBarOptions {
    data object Hidden : NavigationBarOptions
    data class Visible(
        val selectedItem: AdminNavigationBarItem,
        val navController: NavController
    ) : NavigationBarOptions
}