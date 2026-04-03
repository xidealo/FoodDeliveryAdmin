package com.bunbeauty.presentation.feature.mapdelivery.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.bunbeauty.presentation.feature.mapdelivery.MapDeliveryZoneRouteScreen
import kotlinx.serialization.Serializable

@Serializable
data object MapDeliveryZoneScreenDestination

fun NavController.navigateToMapDeliveryZoneScreen(navOptions: NavOptions) = navigate(route = MapDeliveryZoneScreenDestination, navOptions)

fun NavGraphBuilder.mapDeliveryZoneScreenRoute(
    goBack: () -> Unit,
    goToEditDeliveryZoneInfo: (String) -> Unit,
) {
    composable<MapDeliveryZoneScreenDestination> { backStackEntry ->
        MapDeliveryZoneRouteScreen(
            savedStateHandle = backStackEntry.savedStateHandle,
            goBack = goBack,
            goToEditDeliveryZoneInfo = goToEditDeliveryZoneInfo,
        )
    }
}
