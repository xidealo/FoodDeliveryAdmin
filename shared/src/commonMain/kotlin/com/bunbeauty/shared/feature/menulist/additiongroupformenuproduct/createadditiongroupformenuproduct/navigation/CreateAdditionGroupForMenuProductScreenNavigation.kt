package com.bunbeauty.shared.feature.menulist.additiongroupformenuproduct.createadditiongroupformenuproduct.navigation

import androidx.compose.ui.unit.Dp
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.fadeOut
import com.bunbeauty.shared.designsystem.NavAnimationSpec.navAnimationSpecDurationForEnterFade
import com.bunbeauty.shared.designsystem.NavAnimationSpec.navAnimationSpecDurationForSlide
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.bunbeauty.shared.feature.menulist.additiongroupformenuproduct.createadditiongroupformenuproduct.CreateAdditionGroupForMenuProductRouteScreen
import kotlinx.serialization.Serializable

@Serializable
data class CreateAdditionGroupForMenuProductScreenDestination(
    val menuProductUuid: String,
)

fun NavController.navigateToCreateAdditionGroupForMenuProductScreen(
    menuProductUuid: String,
    navOptions: NavOptions,
) = navigate(route = CreateAdditionGroupForMenuProductScreenDestination(menuProductUuid = menuProductUuid), navOptions)

fun NavGraphBuilder.createAdditionGroupForMenuProductScreenRoute(
    showInfoMessage: (String, Dp) -> Unit,
    goBack: () -> Unit,
    goToSelectAdditionGroup: (String, String, String?) -> Unit,
    goToSelectAdditionList: (String?, String, String, List<String>) -> Unit,
) {
    composable<CreateAdditionGroupForMenuProductScreenDestination>(
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
        CreateAdditionGroupForMenuProductRouteScreen(
            showInfoMessage = showInfoMessage,
            goBack = goBack,
            goToSelectAdditionGroup = goToSelectAdditionGroup,
            goToSelectAdditionList = goToSelectAdditionList,
            backStackEntry = backStackEntry,
        )
    }
}
