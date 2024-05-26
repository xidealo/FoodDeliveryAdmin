package com.bunbeauty.fooddeliveryadmin.screen.gallery

import android.os.Bundle
import android.view.View
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.compose.AdminScaffold
import com.bunbeauty.fooddeliveryadmin.compose.element.card.AdminCard
import com.bunbeauty.fooddeliveryadmin.compose.screen.LoadingScreen
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme
import com.bunbeauty.fooddeliveryadmin.coreui.BaseComposeFragment
import com.bunbeauty.presentation.feature.gallery.Gallery
import com.bunbeauty.presentation.feature.gallery.GalleryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList

@AndroidEntryPoint
class GalleryFragment :
    BaseComposeFragment<Gallery.DataState, GalleryViewState, Gallery.Action, Gallery.Event>() {

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
            pullRefreshEnabled = true,
            refreshing = state.isRefreshing,
            onRefresh = {
                viewModel.onAction(Gallery.Action.Refresh)
            },
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
                            state.photos
                        ) { photoUrl ->
                            AdminCard(
                                elevated = false,
                                shape = RoundedCornerShape(0.dp),
                                onClick = {
                                    onAction(Gallery.Action.OnSelectedPhotoClick(photoUrl = photoUrl))
                                }
                            ) {
                                AsyncImage(
                                    modifier = Modifier,
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(photoUrl)
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
    }

    @Preview
    @Composable
    fun GalleryScreenPreview() {
        AdminTheme {
            GalleryScreen(
                state = GalleryViewState(
                    photos = persistentListOf("https://vk.com/xidealo?z=photo48589095_457256794%2Falbum48589095_0%2Frev"),
                    isLoading = false,
                    isRefreshing = false,
                    hasError = false
                ),
                onAction = {}
            )
        }
    }

    @Composable
    override fun mapState(state: Gallery.DataState): GalleryViewState {
        return GalleryViewState(
            photos = state.photoList
                .map { photo -> photo.url }
                .toPersistentList(),
            isLoading = state.isLoading,
            hasError = state.hasError,
            isRefreshing = state.isRefreshing
        )
    }

    override fun handleEvent(event: Gallery.Event) {
        when (event) {
            Gallery.Event.Back -> {
                findNavController().popBackStack()
            }

            is Gallery.Event.SelectPhoto -> findNavController().navigate(
                GalleryFragmentDirections.toSelectPhotoFragment(
                    event.photoUrl
                )
            )
        }
    }
}
