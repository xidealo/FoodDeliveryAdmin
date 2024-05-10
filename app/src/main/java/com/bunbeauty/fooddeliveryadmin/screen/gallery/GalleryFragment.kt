package com.bunbeauty.fooddeliveryadmin.screen.gallery

import android.os.Bundle
import android.view.View
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.compose.AdminScaffold
import com.bunbeauty.fooddeliveryadmin.compose.screen.LoadingScreen
import com.bunbeauty.fooddeliveryadmin.coreui.BaseComposeFragment
import com.bunbeauty.presentation.feature.gallery.Gallery
import com.bunbeauty.presentation.feature.gallery.GalleryViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GalleryFragment :
    BaseComposeFragment<Gallery.ViewDataState, GalleryViewState, Gallery.Action, Gallery.Event>() {

    override val viewModel: GalleryViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.onAction(Gallery.Action.Init)
    }

    @Composable
    override fun Screen(state: GalleryViewState, onAction: (Gallery.Action) -> Unit) {
        GalleryScreen(state = state, onAction = onAction)
    }

    @Composable
    fun GalleryScreen(
        state: GalleryViewState,
        onAction: (Gallery.Action) -> Unit
    ) {
        AdminScaffold(
            title = stringResource(R.string.title_gallery),
            backActionClick = {
                onAction(Gallery.Action.Back)
            }
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
                        verticalArrangement = Arrangement.Absolute.spacedBy(space = 8.dp)
                    ) {
                        items(
                            state.photos,
                            key = { photoLink ->
                                photoLink
                            }
                        ) { photoLink ->
                            AsyncImage(
                                modifier = Modifier,
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(photoLink)
                                    .crossfade(true)
                                    .build(),
                                placeholder = painterResource(R.drawable.default_product),
                                contentDescription = stringResource(R.string.description_product),
                                contentScale = ContentScale.FillWidth
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    override fun mapState(state: Gallery.ViewDataState): GalleryViewState {
        return GalleryViewState(
            photos = state.photoList.map { photo -> photo.photoLink },
            isLoading = state.isLoading,
            hasError = state.hasError
        )
    }

    override fun handleEvent(event: Gallery.Event) {
        when (event) {
            Gallery.Event.Back -> {
                findNavController().popBackStack()
            }
        }
    }
}