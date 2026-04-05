package com.bunbeauty.presentation.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.bunbeauty.presentation.feature.login.navigation.LoginScreenDestination

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun FoodDeliveryNavHost(
    navController: NavHostController,
    showInfoMessage: (String, Dp) -> Unit,
    showErrorMessage: (String) -> Unit,
) {
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
            cancelNotification = {},
        )
    }
}
