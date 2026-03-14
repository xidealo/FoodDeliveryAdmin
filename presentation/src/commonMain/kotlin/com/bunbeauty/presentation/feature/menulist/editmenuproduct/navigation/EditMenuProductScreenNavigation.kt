package com.bunbeauty.presentation.feature.menulist.editmenuproduct.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.bunbeauty.presentation.feature.menulist.editmenuproduct.EditMenuProductRouteScreen
import kotlinx.serialization.Serializable

@Serializable
data class EditMenuProductScreenDestination(
    val menuProductUuid: String,
)

fun NavController.navigateToEditMenuProductScreen(
    menuProductUuid: String,
    navOptions: NavOptions,
) = navigate(route = EditMenuProductScreenDestination(menuProductUuid = menuProductUuid), navOptions)

fun NavGraphBuilder.editMenuProductScreenRoute(
    showInfoMessage: (String, Int) -> Unit,
    showErrorMessage: (String) -> Unit,
    goBack: () -> Unit,
    goToCategoryList: (List<String>) -> Unit,
    goToAdditionList: (String) -> Unit,
    goToCropImage: (String) -> Unit,
) {
    composable<EditMenuProductScreenDestination> { backStackEntry ->
        EditMenuProductRouteScreen(
            showInfoMessage = showInfoMessage,
            showErrorMessage = showErrorMessage,
            goBack = goBack,
            goToCategoryList = goToCategoryList,
            goToAdditionList = goToAdditionList,
            goToCropImage = goToCropImage,
            backStackEntry = backStackEntry,
        )
    }
}
