package com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct.editadditiongroupformenuproduct.navigation

import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct.editadditiongroupformenuproduct.EditAdditionGroupForMenuProductRouteScreen
import kotlinx.serialization.Serializable

@Serializable
data class EditAdditionGroupForMenuProductScreenDestination(
    val additionGroupUuid: String,
    val menuProductUuid: String,
)

fun NavController.navigateToEditAdditionGroupForMenuProductScreen(
    additionGroupUuid: String,
    menuProductUuid: String,
    navOptions: NavOptions,
) = navigate(
    route = EditAdditionGroupForMenuProductScreenDestination(additionGroupUuid = additionGroupUuid, menuProductUuid = menuProductUuid),
    navOptions,
)

fun NavGraphBuilder.editAdditionGroupForMenuProductScreenRoute(
    showInfoMessage: (String, Dp) -> Unit,
    goBack: () -> Unit,
    goToSelectAdditionGroup: (String, String, String) -> Unit,
    goToSelectAdditionList: (String, String, String, List<String>?) -> Unit,
) {
    composable<EditAdditionGroupForMenuProductScreenDestination> { backStackEntry ->
        EditAdditionGroupForMenuProductRouteScreen(
            showInfoMessage = showInfoMessage,
            goBack = goBack,
            goToSelectAdditionGroup = goToSelectAdditionGroup,
            goToSelectAdditionList = goToSelectAdditionList,
            backStackEntry = backStackEntry,
        )
    }
}
