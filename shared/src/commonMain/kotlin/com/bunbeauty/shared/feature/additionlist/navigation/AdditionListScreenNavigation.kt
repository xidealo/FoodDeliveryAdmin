package com.bunbeauty.shared.feature.additionlist.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.fadeOut
import com.bunbeauty.shared.designsystem.NavAnimationSpec.navAnimationSpecDurationForEnterFade
import com.bunbeauty.shared.designsystem.NavAnimationSpec.navAnimationSpecDurationForSlide
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.bunbeauty.shared.feature.additionlist.AdditionListRouteScreen
import kotlinx.serialization.Serializable

@Serializable
data object AdditionListScreenDestination

fun NavController.navigateToAdditionListScreen(navOptions: NavOptions) = navigate(route = AdditionListScreenDestination, navOptions)

fun NavGraphBuilder.additionListScreenRoute(
    goBack: () -> Unit,
    goToCreateAdditionScreen: () -> Unit,
    goToEditAdditionScreen: (String) -> Unit,
) {
    composable<AdditionListScreenDestination>(
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
        AdditionListRouteScreen(
            goBack = goBack,
            goToCreateAdditionScreen = goToCreateAdditionScreen,
            goToEditAdditionScreen = goToEditAdditionScreen,
        )
    }
}
