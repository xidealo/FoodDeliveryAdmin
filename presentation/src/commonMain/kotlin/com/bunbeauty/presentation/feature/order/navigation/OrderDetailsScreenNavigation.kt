package com.bunbeauty.presentation.feature.order.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.bunbeauty.presentation.feature.order.OrderDetailsRouteScreen
import kotlinx.serialization.Serializable

@Serializable
data class OrderDetailsScreenDestination(
    val orderUuid: String,
    val orderCode: String,
)

fun NavController.navigateToOrderDetailsScreen(
    orderUuid: String,
    orderCode: String,
    navOptions: NavOptions,
) = navigate(
    route =
        OrderDetailsScreenDestination(
            orderUuid = orderUuid,
            orderCode = orderCode,
        ),
    navOptions,
)

fun NavGraphBuilder.orderDetailsScreenRoute(
    showInfoMessage: (String, Int) -> Unit,
    showErrorMessage: (String) -> Unit,
    goBack: () -> Unit,
    onCallPhone: (String) -> Unit,
    onCancellationConfirmed: () -> Unit,
) {
    composable<OrderDetailsScreenDestination> {
        OrderDetailsRouteScreen(
            showInfoMessage = showInfoMessage,
            showErrorMessage = showErrorMessage,
            goBack = goBack,
            onCallPhone = onCallPhone,
            onCancellationConfirmed = onCancellationConfirmed,
            backStackEntry = it,
        )
    }
}
