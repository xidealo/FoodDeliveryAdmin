package com.bunbeauty.shared.feature.menulist.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.fadeOut
import com.bunbeauty.shared.designsystem.NavAnimationSpec.navAnimationSpecDurationForEnterFade
import com.bunbeauty.shared.designsystem.NavAnimationSpec.navAnimationSpecDurationForSlide
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.bunbeauty.shared.feature.menulist.MenuListRouteScreen
import kotlinx.serialization.Serializable

@Serializable
data object MenuListScreenDestination

fun NavController.navigateToMenuListScreen(navOptions: NavOptions) = navigate(route = MenuListScreenDestination, navOptions)

fun NavGraphBuilder.menuListScreenRoute(
    goToCreateMenuProductScreen: () -> Unit,
    goToEditMenuProductScreen: (String) -> Unit,
    back: () -> Unit,
) {
    composable<MenuListScreenDestination>(
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
        MenuListRouteScreen(
            goToCreateMenuProductScreen = goToCreateMenuProductScreen,
            goToEditMenuProductScreen = goToEditMenuProductScreen,
            back = back,
        )
    }
}
