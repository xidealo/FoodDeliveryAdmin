package com.bunbeauty.shared.feature.gallery

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bunbeauty.shared.designsystem.compose.AdminScaffold
import com.bunbeauty.shared.designsystem.compose.element.card.AdminCard
import com.bunbeauty.shared.designsystem.compose.element.image.AdminAsyncImage
import com.bunbeauty.shared.designsystem.compose.element.image.ImageData
import com.bunbeauty.shared.designsystem.compose.screen.LoadingScreen
import fooddeliveryadmin.shared.generated.resources.Res
import fooddeliveryadmin.shared.generated.resources.description_product
import fooddeliveryadmin.shared.generated.resources.title_gallery
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun GalleryRouteScreen(
    viewModel: GalleryViewModel = koinViewModel(),
    goBack: () -> Unit,
) {
    val viewState by viewModel.state.collectAsStateWithLifecycle()
    val onAction =
        remember {
            { event: Gallery.Action ->
                viewModel.onAction(event)
            }
        }

    val effects by viewModel.events.collectAsStateWithLifecycle()
    val consumeEffects =
        remember {
            {
                viewModel.consumeEvents(effects)
            }
        }

    LaunchedEffect(Unit) {
        onAction(Gallery.Action.Init)
    }

    GalleryEffect(
        effects = effects,
        consumeEffects = consumeEffects,
        goBack = goBack,
    )

    GalleryScreen(
        state = viewState.toViewState(),
        onAction = onAction,
    )
}

@Composable
private fun GalleryEffect(
    effects: List<Gallery.Event>,
    goBack: () -> Unit,
    consumeEffects: () -> Unit,
) {
    LaunchedEffect(effects) {
        effects.forEach { effect ->
            when (effect) {
                Gallery.Event.Back -> {
                    goBack()
                }
            }
        }
        consumeEffects()
    }
}

@Composable
fun GalleryScreen(
    state: GalleryViewState,
    onAction: (Gallery.Action) -> Unit,
) {
    AdminScaffold(
        title = stringResource(Res.string.title_gallery),
        pullRefreshEnabled = true,
        refreshing = state.isRefreshing,
        onRefresh = {
            onAction(Gallery.Action.Refresh)
        },
        backActionClick = {
            onAction(Gallery.Action.Back)
        },
    ) {
        when {
            state.isLoading -> {
                LoadingScreen()
            }

            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(count = 2),
                    contentPadding = PaddingValues(all = 16.dp),
                    horizontalArrangement = Arrangement.Absolute.spacedBy(space = 8.dp),
                    verticalArrangement = Arrangement.Absolute.spacedBy(space = 8.dp),
                ) {
                    items(
                        state.photos,
                    ) { photoUrl ->
                        AdminCard(
                            elevated = false,
                            shape = RoundedCornerShape(0.dp),
                            onClick = {
                                onAction(Gallery.Action.OnSelectedPhotoClick(photoUrl = photoUrl))
                            },
                        ) {
                            AdminAsyncImage(
                                imageData = ImageData.HttpUrl(photoUrl),
                                contentDescription = Res.string.description_product,
                            )
                        }
                    }
                }
            }
        }
    }
}
