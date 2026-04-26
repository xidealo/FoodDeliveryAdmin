package com.bunbeauty.shared.feature.profile.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.bunbeauty.shared.feature.profile.ProfileRouteScreen
import kotlinx.serialization.Serializable

@Serializable
data object ProfileScreenDestination

fun NavController.navigateToProfileScreen(navOptions: NavOptions) = navigate(route = ProfileScreenDestination, navOptions)

fun NavGraphBuilder.profileScreenRoute(
    showErrorMessage: (String) -> Unit,
    goToSettingsScreen: () -> Unit,
    goToStatisticScreen: () -> Unit,
    goToMenuScreen: () -> Unit,
    goToMapScreen: () -> Unit,
    goToLoginScreen: () -> Unit,
    goBack: () -> Unit,
) {
    composable<ProfileScreenDestination> {
        ProfileRouteScreen(
            showErrorMessage = showErrorMessage,
            goToSettingsScreen = goToSettingsScreen,
            goToStatisticScreen = goToStatisticScreen,
            goToMenuScreen = goToMenuScreen,
            goToMapScreen = goToMapScreen,
            goToLoginScreen = goToLoginScreen,
            goBack = goBack,
        )
    }
}
