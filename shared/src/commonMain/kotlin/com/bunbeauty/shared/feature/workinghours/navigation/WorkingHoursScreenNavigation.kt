package com.bunbeauty.shared.feature.workinghours.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.fadeOut
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.bunbeauty.shared.designsystem.NavAnimationSpec.navAnimationSpecDurationForEnterFade
import com.bunbeauty.shared.designsystem.NavAnimationSpec.navAnimationSpecDurationForSlide
import com.bunbeauty.shared.feature.workinghours.WorkingHoursRouteScreen
import kotlinx.serialization.Serializable

@Serializable
data object WorkingHoursScreenDestination

fun NavController.navigateToWorkingHoursScreen(navOptions: NavOptions) = navigate(route = WorkingHoursScreenDestination, navOptions)

fun NavGraphBuilder.workingHoursScreenRoute(
    goBack: () -> Unit,
    showInfoMessage: (String, Dp) -> Unit,
) {
    composable<WorkingHoursScreenDestination>(
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
        WorkingHoursRouteScreen(
            goBack = goBack,
            showInfoMessage = showInfoMessage,
        )
    }
}
