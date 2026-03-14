package com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct.selectadditiongroup.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct.selectadditiongroup.SelectAdditionGroupRouteScreen
import kotlinx.serialization.Serializable

@Serializable
data class SelectAdditionGroupScreenDestination(
    val additionGroupUuid: String?,
    val menuProductUuid: String,
    val mainEditedAdditionGroupUuid: String?,
)

fun NavController.navigateToSelectAdditionGroupScreen(
    additionGroupUuid: String?,
    menuProductUuid: String,
    mainEditedAdditionGroupUuid: String?,
    navOptions: NavOptions,
) = navigate(
    route =
        SelectAdditionGroupScreenDestination(
            additionGroupUuid = additionGroupUuid,
            menuProductUuid = menuProductUuid,
            mainEditedAdditionGroupUuid = mainEditedAdditionGroupUuid,
        ),
    navOptions,
)

fun NavGraphBuilder.selectAdditionGroupScreenRoute(
    showInfoMessage: (String, Int) -> Unit,
    goBack: () -> Unit,
    onAdditionGroupSelected: (String, String) -> Unit,
) {
    composable<SelectAdditionGroupScreenDestination> { backStackEntry ->
        SelectAdditionGroupRouteScreen(
            showInfoMessage = showInfoMessage,
            goBack = goBack,
            onAdditionGroupSelected = onAdditionGroupSelected,
            backStackEntry = backStackEntry,
        )
    }
}
