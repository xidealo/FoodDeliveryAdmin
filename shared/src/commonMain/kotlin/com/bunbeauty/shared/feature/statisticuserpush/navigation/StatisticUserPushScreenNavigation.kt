package com.bunbeauty.shared.feature.statisticuserpush.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.fadeOut
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.bunbeauty.shared.designsystem.NavAnimationSpec.navAnimationSpecDurationForEnterFade
import com.bunbeauty.shared.designsystem.NavAnimationSpec.navAnimationSpecDurationForSlide
import com.bunbeauty.shared.feature.statisticuserpush.StatisticUserPushRouteScreen
import kotlinx.serialization.Serializable

@Serializable
data class StatisticUserPushScreenDestination(
    val phoneNumber: String,
)

fun NavController.navigateToStatisticUserPushScreen(
    phoneNumber: String,
    navOptions: NavOptions,
) = navigate(
    route =
        StatisticUserPushScreenDestination(
            phoneNumber = phoneNumber,
        ),
    navOptions = navOptions,
)

fun NavGraphBuilder.statisticUserPushScreenRoute(
    goBack: () -> Unit,
    showInfoMessage: (String, Dp) -> Unit,
) {
    composable<StatisticUserPushScreenDestination>(
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
        StatisticUserPushRouteScreen(
            backStackEntry = it,
            goBack = goBack,
            showInfoMessage = showInfoMessage,
        )
    }
}
