package com.bunbeauty.presentation.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navOptions
import com.bunbeauty.presentation.feature.login.navigation.loginScreenRoute

internal val emptyNavOptions = navOptions { }

@OptIn(ExperimentalSharedTransitionApi::class)
fun NavGraphBuilder.foodDeliveryNavGraphBuilder(
    navController: NavController,
    showInfoMessage: (String, Int) -> Unit,
    showErrorMessage: (String) -> Unit,
) {
    loginScreenRoute(
        showErrorMessage = showErrorMessage,
        goToOrderListScreen = {
            //navigate to order list
        },
    )

}
