package com.bunbeauty.presentation.feature.menulist.cropimage.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.bunbeauty.presentation.feature.menulist.cropimage.CropImagePreset
import com.bunbeauty.presentation.feature.menulist.cropimage.CropImageRouteScreen
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel

@Serializable
data class CropImageScreenDestination(
    val uri: String,
    val preset: CropImagePreset,
)

fun NavController.navigateToCropImageScreen(
    uri: String,
    preset: CropImagePreset,
    navOptions: NavOptions,
) = navigate(
    route =
        CropImageScreenDestination(
            uri = uri,
            preset = preset,
        ),
    navOptions,
)

fun NavGraphBuilder.cropImageScreenRoute(
    goBack: () -> Unit,
    onCropSaved: (String) -> Unit,
) {
    composable<CropImageScreenDestination> { backStackEntry ->
        val destination = backStackEntry.toRoute<CropImageScreenDestination>()
        CropImageRouteScreen(
            viewModel = koinViewModel(),
            goBack = goBack,
            onCropSaved = onCropSaved,
            uri = destination.uri,
            preset = destination.preset,
        )
    }
}
