package com.bunbeauty.shared.feature.additionlist.editadditionlist.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.fadeOut
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.bunbeauty.shared.designsystem.NavAnimationSpec.navAnimationSpecDurationForEnterFade
import com.bunbeauty.shared.designsystem.NavAnimationSpec.navAnimationSpecDurationForSlide
import com.bunbeauty.shared.feature.additionlist.editadditionlist.EditAdditionRouteScreen
import kotlinx.serialization.Serializable

@Serializable
data class EditAdditionScreenDestination(
    val additionUuid: String,
)

fun NavController.navigateToEditAdditionScreen(
    additionUuid: String,
    navOptions: NavOptions,
) = navigate(
    route =
        EditAdditionScreenDestination(
            additionUuid = additionUuid,
        ),
    navOptions,
)

fun NavGraphBuilder.editAdditionScreenRoute(
    goBack: () -> Unit,
    showInfoMessage: (String, Dp) -> Unit,
    goToCropImage: (String) -> Unit,
) {
    composable<EditAdditionScreenDestination>(
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
        EditAdditionRouteScreen(
            showInfoMessage = showInfoMessage,
            goBack = goBack,
            goToCropImage = goToCropImage,
            additionUuid = it.toRoute<EditAdditionScreenDestination>().additionUuid,
            savedStateHandle = it.savedStateHandle,
        )
    }
}
