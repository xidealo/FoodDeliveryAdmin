package com.bunbeauty.presentation.feature.additiongrouplist.createadditiondrouplist.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.bunbeauty.presentation.feature.additiongrouplist.createadditiondrouplist.CreateAdditionGroupRouteScreen
import kotlinx.serialization.Serializable

@Serializable
data object CreateAdditionGroupScreenDestination

fun NavController.navigateToCreateAdditionGroupScreen(navOptions: NavOptions) =
    navigate(route = CreateAdditionGroupScreenDestination, navOptions)

fun NavGraphBuilder.createAdditionGroupScreenRoute(
    showInfoMessage: (String, Int) -> Unit,
    goBack: () -> Unit,
) {
    composable<CreateAdditionGroupScreenDestination> {
        CreateAdditionGroupRouteScreen(
            showInfoMessage = showInfoMessage,
            goBack = goBack,
        )
    }
}
