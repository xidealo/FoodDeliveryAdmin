package com.bunbeauty.shared.feature.profile.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.fadeOut
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.bunbeauty.shared.designsystem.NavAnimationSpec.navAnimationSpecDurationForEnterFade
import com.bunbeauty.shared.designsystem.NavAnimationSpec.navAnimationSpecDurationForSlide
import com.bunbeauty.shared.feature.profile.ProfileRouteScreen
import kotlinx.serialization.Serializable

@Serializable
data object ProfileScreenDestination

fun NavController.navigateToProfileScreen(navOptions: NavOptions) = navigate(route = ProfileScreenDestination, navOptions)

fun NavGraphBuilder.profileScreenRoute(
    showErrorMessage: (String) -> Unit,
    goToSettingsScreen: () -> Unit,
    goToStatisticScreen: () -> Unit,
    goToMenuScreen: () -> Unit,
    goToMapScreen: () -> Unit,
    goToLoginScreen: () -> Unit,
    goBack: () -> Unit,
) {
    composable<ProfileScreenDestination>(
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
        ProfileRouteScreen(
            showErrorMessage = showErrorMessage,
            goToSettingsScreen = goToSettingsScreen,
            goToStatisticScreen = goToStatisticScreen,
            goToMenuScreen = goToMenuScreen,
            goToMapScreen = goToMapScreen,
            goToLoginScreen = goToLoginScreen,
            goBack = goBack,
        )
    }
}
