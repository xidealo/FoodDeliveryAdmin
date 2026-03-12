package com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct.selectaddition.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct.selectaddition.SelectAdditionListRouteScreen
import kotlinx.serialization.Serializable

@Serializable
data class SelectAdditionListScreenDestination(
    val menuProductUuid: String,
    val additionGroupUuid: String?,
    val additionGroupName: String,
    val editedAdditionListUuid: List<String>?,
)

fun NavController.navigateToSelectAdditionListScreen(
    menuProductUuid: String,
    additionGroupUuid: String?,
    additionGroupName: String,
    editedAdditionListUuid: List<String>?,
    navOptions: NavOptions,
) = navigate(
    route = SelectAdditionListScreenDestination(
        menuProductUuid = menuProductUuid,
        additionGroupUuid = additionGroupUuid,
        additionGroupName = additionGroupName,
        editedAdditionListUuid = editedAdditionListUuid,
    ),
    navOptions,
)

fun NavGraphBuilder.selectAdditionListScreenRoute(
    showInfoMessage: (String, Int) -> Unit,
    showErrorMessage: (String) -> Unit,
    goBack: () -> Unit,
    onAdditionListSelected: (List<String>) -> Unit,
) {
    composable<SelectAdditionListScreenDestination> { backStackEntry ->
        SelectAdditionListRouteScreen(
            showInfoMessage = showInfoMessage,
            showErrorMessage = showErrorMessage,
            goBack = goBack,
            onAdditionListSelected = onAdditionListSelected,
            backStackEntry = backStackEntry,
        )
    }
}
