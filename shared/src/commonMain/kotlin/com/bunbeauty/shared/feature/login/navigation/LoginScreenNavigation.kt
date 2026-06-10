package com.bunbeauty.shared.feature.login.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.fadeOut
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import com.bunbeauty.shared.designsystem.NavAnimationSpec.navAnimationSpecDurationForEnterFade
import com.bunbeauty.shared.designsystem.NavAnimationSpec.navAnimationSpecDurationForSlide
import com.bunbeauty.shared.feature.login.LoginRouteScreen
import kotlinx.serialization.Serializable

@Serializable
data object LoginScreenDestination

fun NavController.navigateToLoginScreen(navOptions: NavOptions) = navigate(route = LoginScreenDestination, navOptions)

fun NavController.navigateToLoginScreenClearingBackStack() {
    navigateToLoginScreen(
        navOptions =
            navOptions {
                popUpTo(graph.id) {
                    inclusive = true
                }
                launchSingleTop = true
            },
    )
}

fun NavGraphBuilder.loginScreenRoute(
    showErrorMessage: (String) -> Unit,
    goToOrderListScreen: () -> Unit,
) {
    composable<LoginScreenDestination>(
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
        LoginRouteScreen(
            showErrorMessage = showErrorMessage,
            goToOrderListScreen = goToOrderListScreen,
        )
    }
}
