package com.bunbeauty.shared.feature.category.createcategory.navigation

import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.bunbeauty.shared.feature.category.createcategory.CreateCategoryRouteScreen
import kotlinx.serialization.Serializable

@Serializable
data object CreateCategoryScreenDestination

fun NavController.navigateToCreateCategoryScreen(navOptions: NavOptions) = navigate(route = CreateCategoryScreenDestination, navOptions)

fun NavGraphBuilder.createCategoryScreenRoute(
    showInfoMessage: (String, Dp) -> Unit,
    goBack: () -> Unit,
) {
    composable<CreateCategoryScreenDestination> {
        CreateCategoryRouteScreen(
            showInfoMessage = showInfoMessage,
            goBack = goBack,
        )
    }
}
