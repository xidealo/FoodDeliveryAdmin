package com.bunbeauty.shared.feature.menulist.createmenuproduct.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.fadeOut
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.bunbeauty.shared.designsystem.NavAnimationSpec.navAnimationSpecDurationForEnterFade
import com.bunbeauty.shared.designsystem.NavAnimationSpec.navAnimationSpecDurationForSlide
import com.bunbeauty.shared.feature.menulist.createmenuproduct.CreateMenuProductRouteScreen
import kotlinx.serialization.Serializable

@Serializable
data object CreateMenuProductScreenDestination

fun NavController.navigateToCreateMenuProductScreen(navOptions: NavOptions) =
    navigate(route = CreateMenuProductScreenDestination, navOptions)

fun NavGraphBuilder.createMenuProductScreenRoute(
    showInfoMessage: (String, Dp) -> Unit,
    showErrorMessage: (String) -> Unit,
    goBack: () -> Unit,
    goToCategoryList: (List<String>) -> Unit,
    goToCropImage: (String) -> Unit,
) {
    composable<CreateMenuProductScreenDestination>(
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
        CreateMenuProductRouteScreen(
            showInfoMessage = showInfoMessage,
            showErrorMessage = showErrorMessage,
            goBack = goBack,
            goToCategoryList = goToCategoryList,
            goToCropImage = goToCropImage,
            backStackEntry = it,
        )
    }
}
