package com.bunbeauty.shared.feature.orderlist.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.fadeOut
import com.bunbeauty.shared.designsystem.NavAnimationSpec.navAnimationSpecDurationForEnterFade
import com.bunbeauty.shared.designsystem.NavAnimationSpec.navAnimationSpecDurationForSlide
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.bunbeauty.shared.feature.orderlist.OrderListRouteScreen
import kotlinx.serialization.Serializable

@Serializable
data object OrderListScreenNavigation

fun NavController.navigateToOrderListScreen(navOptions: NavOptions) = navigate(route = OrderListScreenNavigation, navOptions)

fun NavGraphBuilder.orderListScreenRoute(
    cancelNotification: (Int) -> Unit,
    openOrderDetails: (String, String) -> Unit,
    goToProfileScreen: () -> Unit,
) {
    composable<OrderListScreenNavigation>(
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
    ) {
        OrderListRouteScreen(
            cancelNotification = cancelNotification,
            openOrderDetails = openOrderDetails,
            goToProfileScreen = goToProfileScreen,
        )
    }
}
