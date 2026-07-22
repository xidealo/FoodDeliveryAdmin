package com.bunbeauty.shared.feature.statisticuserdetails.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.fadeOut
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.bunbeauty.shared.designsystem.NavAnimationSpec.navAnimationSpecDurationForEnterFade
import com.bunbeauty.shared.designsystem.NavAnimationSpec.navAnimationSpecDurationForSlide
import com.bunbeauty.shared.feature.statisticuserdetails.StatisticUserDetailsRouteScreen
import kotlinx.serialization.Serializable

@Serializable
data class StatisticUserDetailsScreenDestination(
    val userUuid: String,
)

fun NavController.navigateToStatisticUserDetailsScreen(
    userUuid: String,
    navOptions: NavOptions,
) = navigate(
    route = StatisticUserDetailsScreenDestination(userUuid = userUuid),
    navOptions = navOptions,
)

fun NavGraphBuilder.statisticUserDetailsScreenRoute(goBack: () -> Unit) {
    composable<StatisticUserDetailsScreenDestination>(
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
        StatisticUserDetailsRouteScreen(
            backStackEntry = it,
            goBack = goBack,
        )
    }
}
