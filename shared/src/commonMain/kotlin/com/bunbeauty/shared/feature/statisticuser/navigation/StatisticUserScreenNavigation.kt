package com.bunbeauty.shared.feature.statisticuser.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.fadeOut
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.bunbeauty.shared.designsystem.NavAnimationSpec.navAnimationSpecDurationForEnterFade
import com.bunbeauty.shared.designsystem.NavAnimationSpec.navAnimationSpecDurationForSlide
import com.bunbeauty.shared.feature.statisticuser.StatisticUserRouteScreen
import kotlinx.serialization.Serializable

@Serializable
data object StatisticUserScreenDestination

fun NavController.navigateToStatisticUserScreen(navOptions: NavOptions) = navigate(route = StatisticUserScreenDestination, navOptions)

fun NavGraphBuilder.statisticUserScreenRoute(
    goToUserDetails: (String) -> Unit,
    goBack: () -> Unit,
) {
    composable<StatisticUserScreenDestination>(
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
        StatisticUserRouteScreen(
            goToUserDetails = goToUserDetails,
            goBack = goBack,
        )
    }
}
