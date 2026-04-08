package com.bunbeauty.shared.feature.menulist.createmenuproduct.navigation

import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.bunbeauty.shared.feature.menulist.createmenuproduct.CreateMenuProductRouteScreen
import kotlinx.serialization.Serializable

@Serializable
data object CreateMenuProductScreenDestination

fun NavController.navigateToCreateMenuProductScreen(navOptions: NavOptions) =
    navigate(route = CreateMenuProductScreenDestination, navOptions)

fun NavGraphBuilder.createMenuProductScreenRoute(
    showInfoMessage: (String, Dp) -> Unit,
    showErrorMessage: (String) -> Unit,
    goBack: () -> Unit,
    goToCategoryList: (List<String>) -> Unit,
    goToCropImage: (String) -> Unit,
) {
    composable<CreateMenuProductScreenDestination> {
        CreateMenuProductRouteScreen(
            showInfoMessage = showInfoMessage,
            showErrorMessage = showErrorMessage,
            goBack = goBack,
            goToCategoryList = goToCategoryList,
            goToCropImage = goToCropImage,
            backStackEntry = it,
        )
    }
}
