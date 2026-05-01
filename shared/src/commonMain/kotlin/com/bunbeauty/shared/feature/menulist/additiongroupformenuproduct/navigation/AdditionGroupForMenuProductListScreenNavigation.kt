package com.bunbeauty.shared.feature.menulist.additiongroupformenuproduct.navigation

import androidx.compose.ui.unit.Dp
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.fadeOut
import com.bunbeauty.shared.designsystem.NavAnimationSpec.navAnimationSpecDurationForEnterFade
import com.bunbeauty.shared.designsystem.NavAnimationSpec.navAnimationSpecDurationForSlide
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.bunbeauty.shared.feature.menulist.additiongroupformenuproduct.AdditionGroupForMenuProductListRouteScreen
import kotlinx.serialization.Serializable

@Serializable
data class AdditionGroupForMenuProductListScreenDestination(
    val menuProductUuid: String,
)

fun NavController.navigateToAdditionGroupForMenuProductListScreen(
    menuProductUuid: String,
    navOptions: NavOptions,
) = navigate(route = AdditionGroupForMenuProductListScreenDestination(menuProductUuid = menuProductUuid), navOptions)

fun NavGraphBuilder.additionGroupForMenuProductListScreenRoute(
    showInfoMessage: (String, Dp) -> Unit,
    goBack: () -> Unit,
    goToCreateAdditionGroup: (String) -> Unit,
    goToEditAdditionGroup: (String, String) -> Unit,
) {
    composable<AdditionGroupForMenuProductListScreenDestination>(
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
        AdditionGroupForMenuProductListRouteScreen(
            showInfoMessage = showInfoMessage,
            goBack = goBack,
            goToCreateAdditionGroup = goToCreateAdditionGroup,
            goToEditAdditionGroup = goToEditAdditionGroup,
            backStackEntry = backStackEntry,
        )
    }
}
