package com.bunbeauty.shared.feature.statisticdetails.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.fadeOut
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.bunbeauty.shared.designsystem.NavAnimationSpec.navAnimationSpecDurationForEnterFade
import com.bunbeauty.shared.designsystem.NavAnimationSpec.navAnimationSpecDurationForSlide
import com.bunbeauty.shared.feature.statistic.TimeIntervalCode
import com.bunbeauty.shared.feature.statisticdetails.StatisticDetailsRouteScreen
import kotlinx.serialization.Serializable

@Serializable
data class StatisticDetailsScreenDestination(
    val date: String,
    val period: TimeIntervalCode = TimeIntervalCode.DAY,
)

fun NavController.navigateToStatisticDetailsScreen(
    date: String,
    period: TimeIntervalCode,
    navOptions: NavOptions,
) = navigate(
    route = StatisticDetailsScreenDestination(date = date, period = period),
    navOptions = navOptions,
)

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
            backStackEntry = it,
        )
    }
}
