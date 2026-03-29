package com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct.createadditiongroupformenuproduct.navigation

import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct.createadditiongroupformenuproduct.CreateAdditionGroupForMenuProductRouteScreen
import kotlinx.serialization.Serializable

@Serializable
data class CreateAdditionGroupForMenuProductScreenDestination(
    val menuProductUuid: String,
)

fun NavController.navigateToCreateAdditionGroupForMenuProductScreen(
    menuProductUuid: String,
    navOptions: NavOptions,
) = navigate(route = CreateAdditionGroupForMenuProductScreenDestination(menuProductUuid = menuProductUuid), navOptions)

fun NavGraphBuilder.createAdditionGroupForMenuProductScreenRoute(
    showInfoMessage: (String, Dp) -> Unit,
    goBack: () -> Unit,
    goToSelectAdditionGroup: (String, String, String?) -> Unit,
    goToSelectAdditionList: (String?, String, String, List<String>) -> Unit,
) {
    composable<CreateAdditionGroupForMenuProductScreenDestination> { backStackEntry ->
        CreateAdditionGroupForMenuProductRouteScreen(
            showInfoMessage = showInfoMessage,
            goBack = goBack,
            goToSelectAdditionGroup = goToSelectAdditionGroup,
            goToSelectAdditionList = goToSelectAdditionList,
            backStackEntry = backStackEntry,
        )
    }
}
