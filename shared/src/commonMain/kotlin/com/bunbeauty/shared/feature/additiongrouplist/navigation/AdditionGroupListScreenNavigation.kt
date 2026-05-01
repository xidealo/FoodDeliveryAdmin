package com.bunbeauty.shared.feature.additiongrouplist.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.fadeOut
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.bunbeauty.shared.designsystem.NavAnimationSpec.navAnimationSpecDurationForEnterFade
import com.bunbeauty.shared.designsystem.NavAnimationSpec.navAnimationSpecDurationForSlide
import com.bunbeauty.shared.feature.additiongrouplist.AdditionGroupListRouteScreen
import kotlinx.serialization.Serializable

@Serializable
data object AdditionGroupListScreenDestination

fun NavController.navigateToAdditionGroupListScreen(navOptions: NavOptions) =
    navigate(route = AdditionGroupListScreenDestination, navOptions)

fun NavGraphBuilder.additionGroupListScreenRoute(
    goBack: () -> Unit,
    goToCreateAdditionGroupScreen: () -> Unit,
    goToEditAdditionGroupScreen: (String) -> Unit,
) {
    composable<AdditionGroupListScreenDestination>(
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
        AdditionGroupListRouteScreen(
            goBack = goBack,
            goToCreateAdditionGroupScreen = goToCreateAdditionGroupScreen,
            goToEditAdditionGroupScreen = goToEditAdditionGroupScreen,
        )
    }
}
