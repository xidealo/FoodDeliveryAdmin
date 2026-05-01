package com.bunbeauty.shared.feature.mapdelivery.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.fadeOut
import com.bunbeauty.shared.designsystem.NavAnimationSpec.navAnimationSpecDurationForEnterFade
import com.bunbeauty.shared.designsystem.NavAnimationSpec.navAnimationSpecDurationForSlide
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.bunbeauty.shared.feature.mapdelivery.MapDeliveryZoneRouteScreen
import kotlinx.serialization.Serializable

@Serializable
data object MapDeliveryZoneScreenDestination

fun NavController.navigateToMapDeliveryZoneScreen(navOptions: NavOptions) = navigate(route = MapDeliveryZoneScreenDestination, navOptions)

fun NavGraphBuilder.mapDeliveryZoneScreenRoute(
    goBack: () -> Unit,
    goToEditDeliveryZoneInfo: (String) -> Unit,
) {
    composable<MapDeliveryZoneScreenDestination>(
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                navAnimationSpecDurationForSlide,
            )
        },
        exitTransition = {
            fadeOut(
                animationSpec = navAnimationSpecDurationForEnterFade,
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                navAnimationSpecDurationForSlide,
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                navAnimationSpecDurationForSlide,
            )
        },
    ) { backStackEntry ->
        MapDeliveryZoneRouteScreen(
            savedStateHandle = backStackEntry.savedStateHandle,
            goBack = goBack,
            goToEditDeliveryZoneInfo = goToEditDeliveryZoneInfo,
        )
    }
}
