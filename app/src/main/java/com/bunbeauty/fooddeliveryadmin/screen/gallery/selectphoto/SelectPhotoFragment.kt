package com.bunbeauty.fooddeliveryadmin.screen.gallery.selectphoto

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.bunbeauty.fooddeliveryadmin.coreui.BaseComposeFragment
import com.bunbeauty.presentation.feature.gallery.selectphoto.SelectPhoto
import com.bunbeauty.presentation.feature.gallery.selectphoto.SelectPhotoViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectPhotoFragment :
    BaseComposeFragment<SelectPhoto.DataState, SelectPhotoViewState, SelectPhoto.Action, SelectPhoto.Event>() {

    override val viewModel: SelectPhotoViewModel by viewModels()

    @Composable
    override fun Screen(state: SelectPhotoViewState, onAction: (SelectPhoto.Action) -> Unit) {
        SelectPhotoSuccess(state = state, onAction = onAction)
    }

    @Composable
    fun SelectPhotoSuccess(state: SelectPhotoViewState, onAction: (SelectPhoto.Action) -> Unit) {

    }

    @Composable
    override fun mapState(state: SelectPhoto.DataState): SelectPhotoViewState {
        return SelectPhotoViewState(
            photo = state.photo,
            isLoading = state.isLoading,
            hasError = state.hasError
        )
    }

    override fun handleEvent(event: SelectPhoto.Event) {
        when (event) {
            SelectPhoto.Event.Back -> {

            }
        }
    }

}