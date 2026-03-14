package com.bunbeauty.presentation.feature.category.editcategory.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.bunbeauty.presentation.feature.category.editcategory.EditCategoryRouteScreen
import kotlinx.serialization.Serializable

private const val CATEGORY_UUID = "categoryUuid"

@Serializable
data class EditCategoryScreenDestination(
    val categoryUuid: String,
)

fun NavController.navigateToEditCategoryScreen(
    categoryUuid: String,
    navOptions: NavOptions,
) = navigate(route = EditCategoryScreenDestination(categoryUuid = categoryUuid), navOptions)

fun NavGraphBuilder.editCategoryScreenRoute(
    showInfoMessage: (String, Int) -> Unit,
    goBack: () -> Unit,
) {
    composable<EditCategoryScreenDestination> {
        EditCategoryRouteScreen(
            showInfoMessage = showInfoMessage,
            goBack = goBack,
        )
    }
}
