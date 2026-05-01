package com.bunbeauty.shared.feature.menulist.additiongroupformenuproduct.editadditiongroupformenuproduct.navigation

import androidx.compose.ui.unit.Dp
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.fadeOut
import com.bunbeauty.shared.designsystem.NavAnimationSpec.navAnimationSpecDurationForEnterFade
import com.bunbeauty.shared.designsystem.NavAnimationSpec.navAnimationSpecDurationForSlide
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.bunbeauty.shared.feature.menulist.additiongroupformenuproduct.editadditiongroupformenuproduct.EditAdditionGroupForMenuProductRouteScreen
import kotlinx.serialization.Serializable

@Serializable
data class EditAdditionGroupForMenuProductScreenDestination(
    val additionGroupUuid: String,
    val menuProductUuid: String,
)

fun NavController.navigateToEditAdditionGroupForMenuProductScreen(
    additionGroupUuid: String,
    menuProductUuid: String,
    navOptions: NavOptions,
) = navigate(
    route = EditAdditionGroupForMenuProductScreenDestination(additionGroupUuid = additionGroupUuid, menuProductUuid = menuProductUuid),
    navOptions,
)

fun NavGraphBuilder.editAdditionGroupForMenuProductScreenRoute(
    showInfoMessage: (String, Dp) -> Unit,
    goBack: () -> Unit,
    goToSelectAdditionGroup: (String, String, String) -> Unit,
    goToSelectAdditionList: (String, String, String, List<String>?) -> Unit,
) {
    composable<EditAdditionGroupForMenuProductScreenDestination>(
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
        EditAdditionGroupForMenuProductRouteScreen(
            showInfoMessage = showInfoMessage,
            goBack = goBack,
            goToSelectAdditionGroup = goToSelectAdditionGroup,
            goToSelectAdditionList = goToSelectAdditionList,
            backStackEntry = backStackEntry,
        )
    }
}
