package com.bunbeauty.shared.feature.mapdelivery.editinfodeliveryzone.navigation

import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.bunbeauty.shared.feature.mapdelivery.editinfodeliveryzone.EditDeliveryZoneInfoRouteScreen
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
    onZoneUpdated: (String) -> Unit,
    goBack: () -> Unit,
) {
    composable<EditDeliveryZoneInfoScreenDestination> {
        EditDeliveryZoneInfoRouteScreen(
            showInfoMessage = showInfoMessage,
            onZoneUpdated = onZoneUpdated,
            goBack = goBack,
        )
    }
}
