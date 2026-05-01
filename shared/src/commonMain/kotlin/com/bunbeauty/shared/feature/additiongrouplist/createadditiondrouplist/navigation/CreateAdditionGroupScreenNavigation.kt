package com.bunbeauty.shared.feature.additiongrouplist.createadditiondrouplist.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.fadeOut
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.bunbeauty.shared.designsystem.NavAnimationSpec.navAnimationSpecDurationForEnterFade
import com.bunbeauty.shared.designsystem.NavAnimationSpec.navAnimationSpecDurationForSlide
import com.bunbeauty.shared.feature.additiongrouplist.createadditiondrouplist.CreateAdditionGroupRouteScreen
import kotlinx.serialization.Serializable

@Serializable
data object CreateAdditionGroupScreenDestination

fun NavController.navigateToCreateAdditionGroupScreen(navOptions: NavOptions) =
    navigate(route = CreateAdditionGroupScreenDestination, navOptions)

fun NavGraphBuilder.createAdditionGroupScreenRoute(
    showInfoMessage: (String, Dp) -> Unit,
    goBack: () -> Unit,
) {
    composable<CreateAdditionGroupScreenDestination>(
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
        CreateAdditionGroupRouteScreen(
            showInfoMessage = showInfoMessage,
            goBack = goBack,
        )
    }
}
