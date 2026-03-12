package com.bunbeauty.presentation.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.bunbeauty.presentation.feature.login.navigation.LoginScreenDestination

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun FoodDeliveryNavHost(
    showInfoMessage: (String, Int) -> Unit,
    showErrorMessage: (String) -> Unit,
    goToOrderList: () -> Unit,
    goToMenu: () -> Unit,
    goToProfile: () -> Unit,
    cancelNotification: (Int) -> Unit,
    openOrderDetails: (String, String) -> Unit,
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = LoginScreenDestination,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None },
    ) {
        foodDeliveryNavGraphBuilder(
            navController = navController,
            showErrorMessage = showErrorMessage,
            showInfoMessage = showInfoMessage,
            cancelNotification = cancelNotification,
            openOrderDetails = openOrderDetails,

            )
    }
}
