package com.bunbeauty.presentation.feature.additiongrouplist.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.bunbeauty.presentation.feature.additiongrouplist.AdditionGroupListRouteScreen
import kotlinx.serialization.Serializable

@Serializable
data object AdditionGroupListScreenDestination

fun NavController.navigateToAdditionGroupListScreen(navOptions: NavOptions) = navigate(route = AdditionGroupListScreenDestination, navOptions)

fun NavGraphBuilder.additionGroupListScreenRoute(
    goBack: () -> Unit,
    goToCreateAdditionGroupScreen: () -> Unit,
    goToEditAdditionGroupScreen: (String) -> Unit,
) {
    composable<AdditionGroupListScreenDestination> {
        AdditionGroupListRouteScreen(
            goBack = goBack,
            goToCreateAdditionGroupScreen = goToCreateAdditionGroupScreen,
            goToEditAdditionGroupScreen = goToEditAdditionGroupScreen,
        )
    }
}
