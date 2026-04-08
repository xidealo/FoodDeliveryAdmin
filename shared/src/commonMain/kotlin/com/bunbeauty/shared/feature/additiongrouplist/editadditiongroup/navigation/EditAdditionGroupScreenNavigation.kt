package com.bunbeauty.shared.feature.additiongrouplist.editadditiongroup.navigation

import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.bunbeauty.shared.feature.additiongrouplist.editadditiongroup.EditAdditionGroupRouteScreen
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
    showInfoMessage: (String, Dp) -> Unit,
    goBack: () -> Unit,
) {
    composable<EditAdditionGroupScreenDestination> {
        EditAdditionGroupRouteScreen(
            showInfoMessage = showInfoMessage,
            goBack = goBack,
        )
    }
}
