package com.bunbeauty.presentation.feature.orderlist.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.bunbeauty.presentation.feature.orderlist.OrderListRouteScreen
import kotlinx.serialization.Serializable

@Serializable
data object OrderListScreenNavigation

fun NavController.navigateToOrderListScreen(navOptions: NavOptions) = navigate(route = OrderListScreenNavigation, navOptions)

fun NavGraphBuilder.orderListScreenRoute(
    cancelNotification: (Int) -> Unit,
    openOrderDetails: (String, String) -> Unit,
) {
    composable<OrderListScreenNavigation> {
        OrderListRouteScreen(
            cancelNotification = cancelNotification,
            openOrderDetails = openOrderDetails,
        )
    }
}
