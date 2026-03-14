package com.bunbeauty.presentation.feature.statistic.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.bunbeauty.presentation.feature.statistic.StatisticRouteScreen
import kotlinx.serialization.Serializable

@Serializable
data object StatisticScreenDestination

fun NavController.navigateToStatisticScreen(navOptions: NavOptions) = navigate(route = StatisticScreenDestination, navOptions)

fun NavGraphBuilder.statisticScreenRoute(goBack: () -> Unit) {
    composable<StatisticScreenDestination> {
        StatisticRouteScreen(
            goBack = goBack,
        )
    }
}
