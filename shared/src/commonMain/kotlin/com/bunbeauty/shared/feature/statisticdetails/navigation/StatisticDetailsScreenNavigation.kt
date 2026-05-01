package com.bunbeauty.shared.feature.statisticdetails.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.fadeOut
import com.bunbeauty.shared.designsystem.NavAnimationSpec.navAnimationSpecDurationForEnterFade
import com.bunbeauty.shared.designsystem.NavAnimationSpec.navAnimationSpecDurationForSlide
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.bunbeauty.shared.feature.statisticdetails.StatisticDetailsRouteScreen
import kotlinx.serialization.Serializable

@Serializable
data object StatisticDetailsScreenDestination

fun NavController.navigateToStatisticDetailsScreen(navOptions: NavOptions) = navigate(route = StatisticDetailsScreenDestination, navOptions)

fun NavGraphBuilder.statisticDetailsScreenRoute(goBack: () -> Unit) {
    composable<StatisticDetailsScreenDestination>(
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
        StatisticDetailsRouteScreen(
            goBack = goBack,
        )
    }
}
