package com.bunbeauty.presentation.feature.menulist.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.bunbeauty.presentation.feature.menulist.MenuListRouteScreen
import kotlinx.serialization.Serializable

@Serializable
data object MenuListScreenDestination

fun NavController.navigateToMenuListScreen(navOptions: NavOptions) = navigate(route = MenuListScreenDestination, navOptions)

fun NavGraphBuilder.menuListScreenRoute(
    goToCreateMenuProductScreen: () -> Unit,
    goToEditMenuProductScreen: (String) -> Unit,
    back: () -> Unit,
) {
    composable<MenuListScreenDestination> {
        MenuListRouteScreen(
            goToCreateMenuProductScreen = goToCreateMenuProductScreen,
            goToEditMenuProductScreen = goToEditMenuProductScreen,
            back = back,
        )
    }
}
