package com.bunbeauty.presentation.feature.additionlist.editadditionlist.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.bunbeauty.presentation.feature.additionlist.editadditionlist.EditAdditionRouteScreen
import kotlinx.serialization.Serializable

@Serializable
data class EditAdditionScreenDestination(
    val additionUuid: String
)

fun NavController.navigateToEditAdditionScreen(additionUuid: String, navOptions: NavOptions) =
    navigate(
        route = EditAdditionScreenDestination(
            additionUuid = additionUuid
        ), navOptions
    )

fun NavGraphBuilder.editAdditionScreenRoute(
    goBack: () -> Unit,
    showInfoMessage: (String, Int) -> Unit,
) {
    composable<EditAdditionScreenDestination> {
        EditAdditionRouteScreen(
            showInfoMessage = showInfoMessage,
            goBack = goBack,
            additionUuid = it.toRoute<EditAdditionScreenDestination>().additionUuid
        )
    }
}
