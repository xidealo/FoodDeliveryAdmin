package com.bunbeauty.shared.feature.menulist.additiongroupformenuproduct.selectadditiongroup.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.fadeOut
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.bunbeauty.shared.designsystem.NavAnimationSpec.navAnimationSpecDurationForEnterFade
import com.bunbeauty.shared.designsystem.NavAnimationSpec.navAnimationSpecDurationForSlide
import com.bunbeauty.shared.feature.menulist.additiongroupformenuproduct.selectadditiongroup.SelectAdditionGroupRouteScreen
import kotlinx.serialization.Serializable

@Serializable
data class SelectAdditionGroupScreenDestination(
    val additionGroupUuid: String?,
    val menuProductUuid: String,
    val mainEditedAdditionGroupUuid: String?,
)

fun NavController.navigateToSelectAdditionGroupScreen(
    additionGroupUuid: String?,
    menuProductUuid: String,
    mainEditedAdditionGroupUuid: String?,
    navOptions: NavOptions,
) = navigate(
    route =
        SelectAdditionGroupScreenDestination(
            additionGroupUuid = additionGroupUuid,
            menuProductUuid = menuProductUuid,
            mainEditedAdditionGroupUuid = mainEditedAdditionGroupUuid,
        ),
    navOptions,
)

fun NavGraphBuilder.selectAdditionGroupScreenRoute(
    showInfoMessage: (String, Dp) -> Unit,
    goBack: () -> Unit,
    onAdditionGroupSelected: (String) -> Unit,
) {
    composable<SelectAdditionGroupScreenDestination>(
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
    ) { backStackEntry ->
        SelectAdditionGroupRouteScreen(
            showInfoMessage = showInfoMessage,
            goBack = goBack,
            onAdditionGroupSelected = onAdditionGroupSelected,
            backStackEntry = backStackEntry,
        )
    }
}
