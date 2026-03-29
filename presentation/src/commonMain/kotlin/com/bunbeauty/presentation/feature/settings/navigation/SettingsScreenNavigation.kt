package com.bunbeauty.presentation.feature.settings.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.bunbeauty.presentation.feature.settings.SettingsRouteScreen
import kotlinx.serialization.Serializable

@Serializable
data object SettingsScreenDestination

fun NavController.navigateToSettingsScreen(navOptions: NavOptions) = navigate(route = SettingsScreenDestination, navOptions)

fun NavGraphBuilder.settingsScreenRoute(
    goBack: () -> Unit,
    showInfoMessage: (String, Int) -> Unit,
) {
    composable<SettingsScreenDestination> {
        SettingsRouteScreen(
            goBack = goBack,
            showInfoMessage = showInfoMessage,
        )
    }
}
