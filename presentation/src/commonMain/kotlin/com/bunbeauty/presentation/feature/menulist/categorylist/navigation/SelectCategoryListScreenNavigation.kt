package com.bunbeauty.presentation.feature.menulist.categorylist.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.bunbeauty.presentation.feature.menulist.categorylist.SelectCategoryListRouteScreen
import kotlinx.serialization.Serializable

@Serializable
data object SelectCategoryListScreenDestination

fun NavController.navigateToSelectCategoryListScreen(navOptions: NavOptions) = navigate(route = SelectCategoryListScreenDestination, navOptions)

fun NavGraphBuilder.selectCategoryListScreenRoute(
    showInfoMessage: (String, Int) -> Unit,
    goBack: () -> Unit,
) {
    composable<SelectCategoryListScreenDestination> {
        SelectCategoryListRouteScreen(
            showInfoMessage = showInfoMessage,
            goBack = goBack,
        )
    }
}
