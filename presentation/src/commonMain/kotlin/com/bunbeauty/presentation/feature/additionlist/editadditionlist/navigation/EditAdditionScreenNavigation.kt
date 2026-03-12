package com.bunbeauty.presentation.feature.additionlist.editadditionlist.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.bunbeauty.presentation.feature.additionlist.editadditionlist.EditAdditionRouteScreen

data object EditAdditionScreenDestination

fun NavController.navigateToEditAdditionScreen(navOptions: NavOptions) = navigate(route = EditAdditionScreenDestination, navOptions)

fun NavGraphBuilder.editAdditionScreenRoute(
    goBack: () -> Unit,
    showInfoMessage: (String, Int) -> Unit,
) {
    composable<EditAdditionScreenDestination> {
        EditAdditionRouteScreen(
            showInfoMessage = showInfoMessage,
            goBack = goBack
        )
    }
}