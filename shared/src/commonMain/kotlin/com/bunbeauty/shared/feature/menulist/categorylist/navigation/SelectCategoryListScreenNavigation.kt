package com.bunbeauty.shared.feature.menulist.categorylist.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.fadeOut
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.bunbeauty.shared.designsystem.NavAnimationSpec.navAnimationSpecDurationForEnterFade
import com.bunbeauty.shared.designsystem.NavAnimationSpec.navAnimationSpecDurationForSlide
import com.bunbeauty.shared.feature.menulist.categorylist.SelectCategoryListRouteScreen
import kotlinx.serialization.Serializable

@Serializable
data class SelectCategoryListScreenDestination(
    val selectedCategoryList: List<String>,
)

fun NavController.navigateToSelectCategoryListScreen(
    selectedCategoryList: List<String>,
    navOptions: NavOptions,
) = navigate(
    route =
        SelectCategoryListScreenDestination(
            selectedCategoryList = selectedCategoryList,
        ),
    navOptions,
)

fun NavGraphBuilder.selectCategoryListScreenRoute(
    showInfoMessage: (String, Dp) -> Unit,
    goBack: () -> Unit,
    onSaveCategoryList: (List<String>) -> Unit,
) {
    composable<SelectCategoryListScreenDestination>(
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
        SelectCategoryListRouteScreen(
            showInfoMessage = showInfoMessage,
            goBack = goBack,
            backStackEntry = it,
            onSaveCategoryList = onSaveCategoryList,
        )
    }
}
