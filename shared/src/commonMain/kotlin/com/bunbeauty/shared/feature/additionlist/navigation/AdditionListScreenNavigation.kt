package com.bunbeauty.shared.feature.additionlist.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.bunbeauty.shared.feature.additionlist.AdditionListRouteScreen
import kotlinx.serialization.Serializable

@Serializable
data object AdditionListScreenDestination

fun NavController.navigateToAdditionListScreen(navOptions: NavOptions) = navigate(route = AdditionListScreenDestination, navOptions)

fun NavGraphBuilder.additionListScreenRoute(
    goBack: () -> Unit,
    goToCreateAdditionScreen: () -> Unit,
    goToEditAdditionScreen: (String) -> Unit,
) {
    composable<AdditionListScreenDestination> {
        AdditionListRouteScreen(
            goBack = goBack,
            goToCreateAdditionScreen = goToCreateAdditionScreen,
            goToEditAdditionScreen = goToEditAdditionScreen,
        )
    }
}
