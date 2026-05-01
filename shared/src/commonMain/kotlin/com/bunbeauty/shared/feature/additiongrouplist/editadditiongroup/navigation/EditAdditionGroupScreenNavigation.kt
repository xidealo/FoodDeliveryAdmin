package com.bunbeauty.shared.feature.additiongrouplist.editadditiongroup.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.fadeOut
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.bunbeauty.shared.designsystem.NavAnimationSpec.navAnimationSpecDurationForEnterFade
import com.bunbeauty.shared.designsystem.NavAnimationSpec.navAnimationSpecDurationForSlide
import com.bunbeauty.shared.feature.additiongrouplist.editadditiongroup.EditAdditionGroupRouteScreen
import kotlinx.serialization.Serializable

@Serializable
data class EditAdditionGroupScreenDestination(
    val additionGroupUuid: String,
)

fun NavController.navigateToEditAdditionGroupScreen(
    additionGroupUuid: String,
    navOptions: NavOptions,
) = navigate(route = EditAdditionGroupScreenDestination(additionGroupUuid = additionGroupUuid), navOptions)

fun NavGraphBuilder.editAdditionGroupScreenRoute(
    showInfoMessage: (String, Dp) -> Unit,
    goBack: () -> Unit,
) {
    composable<EditAdditionGroupScreenDestination>(
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
        EditAdditionGroupRouteScreen(
            showInfoMessage = showInfoMessage,
            goBack = goBack,
        )
    }
}
