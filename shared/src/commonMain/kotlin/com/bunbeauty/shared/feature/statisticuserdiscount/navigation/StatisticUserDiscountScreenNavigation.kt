package com.bunbeauty.shared.feature.statisticuserdiscount.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.fadeOut
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.bunbeauty.shared.designsystem.NavAnimationSpec.navAnimationSpecDurationForEnterFade
import com.bunbeauty.shared.designsystem.NavAnimationSpec.navAnimationSpecDurationForSlide
import com.bunbeauty.shared.feature.statisticuserdiscount.StatisticUserDiscountRouteScreen
import kotlinx.serialization.Serializable

@Serializable
data class StatisticUserDiscountScreenDestination(
    val phoneNumber: String,
    val personalDiscountPercent: Int?,
)

fun NavController.navigateToStatisticUserDiscountScreen(
    phoneNumber: String,
    personalDiscountPercent: Int?,
    navOptions: NavOptions,
) = navigate(
    route =
        StatisticUserDiscountScreenDestination(
            phoneNumber = phoneNumber,
            personalDiscountPercent = personalDiscountPercent,
        ),
    navOptions = navOptions,
)

fun NavGraphBuilder.statisticUserDiscountScreenRoute(
    goBack: () -> Unit,
    onDiscountSaved: (Int) -> Unit,
    showInfoMessage: (String, Dp) -> Unit,
) {
    composable<StatisticUserDiscountScreenDestination>(
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
        StatisticUserDiscountRouteScreen(
            backStackEntry = it,
            goBack = goBack,
            onDiscountSaved = onDiscountSaved,
            showInfoMessage = showInfoMessage,
        )
    }
}
