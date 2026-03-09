package com.bunbeauty.presentation.feature.additiongrouplist.editadditiongroup.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.bunbeauty.presentation.feature.additiongrouplist.editadditiongroup.EditAdditionGroupRouteScreen
import kotlinx.serialization.Serializable

@Serializable
data class EditAdditionGroupScreenDestination(
    val additionGroupUuid: String,
)

fun NavController.navigateToEditAdditionGroupScreen(
    additionGroupUuid: String,
    navOptions: NavOptions,
) = navigate(route = EditAdditionGroupScreenDestination(additionGroupUuid = additionGroupUuid), navOptions)

fun NavGraphBuilder.editAdditionGroupScreenRoute(
    showInfoMessage: (String) -> Unit,
    goBack: () -> Unit,
) {
    composable<EditAdditionGroupScreenDestination> {
        EditAdditionGroupRouteScreen(
            showInfoMessage = showInfoMessage,
            goBack = goBack,
        )
    }
}
