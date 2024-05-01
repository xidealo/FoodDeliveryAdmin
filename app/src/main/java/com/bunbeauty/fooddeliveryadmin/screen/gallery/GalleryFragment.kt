package com.bunbeauty.fooddeliveryadmin.screen.gallery

import androidx.compose.runtime.Composable
import com.bunbeauty.fooddeliveryadmin.coreui.BaseComposeFragment
import com.bunbeauty.presentation.feature.gallery.Gallery
import com.bunbeauty.presentation.viewmodel.base.BaseStateViewModel

class GalleryFragment :
    BaseComposeFragment<Gallery.ViewDataState, GalleryViewState, Gallery.Action, Gallery.Event>() {
    override val viewModel: BaseStateViewModel<Gallery.ViewDataState, Gallery.Action, Gallery.Event>
        get() = TODO("Not yet implemented")

    @Composable
    override fun Screen(state: GalleryViewState, onAction: (Gallery.Action) -> Unit) {
        TODO("Not yet implemented")
    }

    @Composable
    override fun mapState(state: Gallery.ViewDataState): GalleryViewState {
        TODO("Not yet implemented")
    }

    override fun handleEvent(event: Gallery.Event) {
        TODO("Not yet implemented")
    }
}