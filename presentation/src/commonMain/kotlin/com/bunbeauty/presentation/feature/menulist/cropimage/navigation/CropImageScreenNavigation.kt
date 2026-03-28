package com.bunbeauty.presentation.feature.menulist.cropimage.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.bunbeauty.presentation.feature.menulist.cropimage.CropImageRouteScreen
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel


@Serializable
data class CropImageScreenDestination(
    val uri: String,
)

fun NavController.navigateToCropImageScreen(
    uri: String,
    navOptions: NavOptions,
) = navigate(route = CropImageScreenDestination(uri = uri), navOptions)

fun NavGraphBuilder.cropImageScreenRoute(
    goBack: () -> Unit,
    onCropSaved: (String) -> Unit,
) {
    composable<CropImageScreenDestination> { backStackEntry ->
        CropImageRouteScreen(
            viewModel = koinViewModel(),
            goBack = goBack,
            onCropSaved = onCropSaved,
            uri = backStackEntry.toRoute<CropImageScreenDestination>().uri,
        )
    }
}
