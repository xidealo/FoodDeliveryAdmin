package com.bunbeauty.presentation.feature.additionlist.createaddition.navigation

import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.bunbeauty.presentation.feature.additionlist.createaddition.CreateAdditionRouteScreen
import kotlinx.serialization.Serializable

@Serializable
data object CreateAdditionScreenDestination

fun NavController.navigateToCreateAdditionScreen(navOptions: NavOptions) =
    navigate(route = CreateAdditionScreenDestination, navOptions)

fun NavGraphBuilder.createAdditionScreenRoute(
    showInfoMessage: (String, Dp) -> Unit,
    showErrorMessage: (String) -> Unit,
    goBack: () -> Unit,
    goToCropImage: (String) -> Unit,
) {
    composable<CreateAdditionScreenDestination> { backStackEntry ->
        CreateAdditionRouteScreen(
            showInfoMessage = showInfoMessage,
            showErrorMessage = showErrorMessage,
            goBack = goBack,
            goToCropImage = goToCropImage,
            backStackEntry = backStackEntry,
        )
    }
}
