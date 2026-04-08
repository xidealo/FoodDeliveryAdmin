package com.bunbeauty.shared.feature.category.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.bunbeauty.shared.feature.category.CategoryListRouteScreen
import kotlinx.serialization.Serializable

@Serializable
data object CategoryListScreenDestination

fun NavController.navigateToCategoryListScreen(navOptions: NavOptions) = navigate(route = CategoryListScreenDestination, navOptions)

fun NavGraphBuilder.categoryListScreenRoute(
    goBack: () -> Unit,
    goToCreateCategoryScreen: () -> Unit,
    goToEditCategoryScreen: (String) -> Unit,
) {
    composable<CategoryListScreenDestination> {
        CategoryListRouteScreen(
            goBack = goBack,
            goToCreateCategoryScreen = goToCreateCategoryScreen,
            goToEditCategoryScreen = goToEditCategoryScreen,
        )
    }
}
