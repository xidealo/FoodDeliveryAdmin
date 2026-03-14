package com.bunbeauty.presentation.feature.gallery.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.bunbeauty.presentation.feature.gallery.GalleryRouteScreen
import kotlinx.serialization.Serializable

@Serializable
data object GalleryScreenDestination

fun NavController.navigateToGalleryScreen(navOptions: NavOptions) = navigate(route = GalleryScreenDestination, navOptions)

fun NavGraphBuilder.galleryScreenRoute(
    goBack: () -> Unit,
) {
    composable<GalleryScreenDestination> {
        GalleryRouteScreen(
            goBack = goBack,
        )
    }
}
