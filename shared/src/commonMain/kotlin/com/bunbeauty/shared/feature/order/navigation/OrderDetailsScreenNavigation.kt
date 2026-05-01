package com.bunbeauty.shared.feature.order.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.fadeOut
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.bunbeauty.shared.designsystem.NavAnimationSpec.navAnimationSpecDurationForEnterFade
import com.bunbeauty.shared.designsystem.NavAnimationSpec.navAnimationSpecDurationForSlide
import com.bunbeauty.shared.feature.order.OrderDetailsRouteScreen
import kotlinx.serialization.Serializable

@Serializable
data class OrderDetailsScreenDestination(
    val orderUuid: String,
    val orderCode: String,
)

fun NavController.navigateToOrderDetailsScreen(
    orderUuid: String,
    orderCode: String,
    navOptions: NavOptions,
) = navigate(
    route =
        OrderDetailsScreenDestination(
            orderUuid = orderUuid,
            orderCode = orderCode,
        ),
    navOptions,
)

fun NavGraphBuilder.orderDetailsScreenRoute(
    showInfoMessage: (String, Dp) -> Unit,
    showErrorMessage: (String) -> Unit,
    goBack: () -> Unit,
) {
    composable<OrderDetailsScreenDestination>(
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
        OrderDetailsRouteScreen(
            showInfoMessage = showInfoMessage,
            showErrorMessage = showErrorMessage,
            goBack = goBack,
            backStackEntry = it,
        )
    }
}
