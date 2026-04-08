package com.bunbeauty.shared.feature.statisticdetails.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.bunbeauty.shared.feature.statisticdetails.StatisticDetailsRouteScreen
import kotlinx.serialization.Serializable

@Serializable
data object StatisticDetailsScreenDestination

fun NavController.navigateToStatisticDetailsScreen(navOptions: NavOptions) = navigate(route = StatisticDetailsScreenDestination, navOptions)

fun NavGraphBuilder.statisticDetailsScreenRoute(goBack: () -> Unit) {
    composable<StatisticDetailsScreenDestination> {
        StatisticDetailsRouteScreen(
            goBack = goBack,
        )
    }
}
