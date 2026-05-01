package com.bunbeauty.shared.feature.category.createcategory.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.fadeOut
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.bunbeauty.shared.designsystem.NavAnimationSpec.navAnimationSpecDurationForEnterFade
import com.bunbeauty.shared.designsystem.NavAnimationSpec.navAnimationSpecDurationForSlide
import com.bunbeauty.shared.feature.category.createcategory.CreateCategoryRouteScreen
import kotlinx.serialization.Serializable

@Serializable
data object CreateCategoryScreenDestination

fun NavController.navigateToCreateCategoryScreen(navOptions: NavOptions) = navigate(route = CreateCategoryScreenDestination, navOptions)

fun NavGraphBuilder.createCategoryScreenRoute(
    showInfoMessage: (String, Dp) -> Unit,
    goBack: () -> Unit,
) {
    composable<CreateCategoryScreenDestination>(
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
        CreateCategoryRouteScreen(
            showInfoMessage = showInfoMessage,
            goBack = goBack,
        )
    }
}
