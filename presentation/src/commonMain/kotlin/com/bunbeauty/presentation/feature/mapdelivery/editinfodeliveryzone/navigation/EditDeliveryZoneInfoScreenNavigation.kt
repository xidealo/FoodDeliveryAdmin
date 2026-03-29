package com.bunbeauty.presentation.feature.mapdelivery.editinfodeliveryzone.navigation

import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.bunbeauty.presentation.feature.mapdelivery.editinfodeliveryzone.EditDeliveryZoneInfoRouteScreen
import kotlinx.serialization.Serializable

@Serializable
data class EditDeliveryZoneInfoScreenDestination(
    val zoneUuid: String,
)

fun NavController.navigateToEditDeliveryZoneInfoScreen(
    zoneUuid: String,
    navOptions: NavOptions,
) = navigate(route = EditDeliveryZoneInfoScreenDestination(zoneUuid = zoneUuid), navOptions)

fun NavGraphBuilder.editDeliveryZoneInfoScreenRoute(
    showInfoMessage: (String, Dp) -> Unit,
    goBack: () -> Unit,
) {
    composable<EditDeliveryZoneInfoScreenDestination> {
        EditDeliveryZoneInfoRouteScreen(
            showInfoMessage = showInfoMessage,
            goBack = goBack,
        )
    }
}
