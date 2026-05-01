package com.bunbeauty.shared.feature.menu.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.fadeOut
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.bunbeauty.shared.designsystem.NavAnimationSpec.navAnimationSpecDurationForEnterFade
import com.bunbeauty.shared.designsystem.NavAnimationSpec.navAnimationSpecDurationForSlide
import com.bunbeauty.shared.feature.menu.MenuRouteScreen
import kotlinx.serialization.Serializable

@Serializable
data object MenuScreenDestination

fun NavController.navigateToMenuScreen(navOptions: NavOptions) = navigate(route = MenuScreenDestination, navOptions)

fun NavGraphBuilder.menuScreenRoute(
    goToCategoriesScreen: () -> Unit,
    goToMenuListScreen: () -> Unit,
    goToAdditionGroupListScreen: () -> Unit,
    goToAdditionListScreen: () -> Unit,
    goBack: () -> Unit,
) {
    composable<MenuScreenDestination>(
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
        MenuRouteScreen(
            goToCategoriesScreen = goToCategoriesScreen,
            goToMenuListScreen = goToMenuListScreen,
            goToAdditionGroupListScreen = goToAdditionGroupListScreen,
            goToAdditionListScreen = goToAdditionListScreen,
            goBack = goBack,
        )
    }
}
