package com.bunbeauty.fooddeliveryadmin.screen.gallery.selectphoto

import android.os.Bundle
import android.view.View
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.compose.AdminScaffold
import com.bunbeauty.fooddeliveryadmin.compose.element.button.MainButton
import com.bunbeauty.fooddeliveryadmin.compose.element.card.AdminCardDefaults
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme
import com.bunbeauty.fooddeliveryadmin.coreui.BaseComposeFragment
import com.bunbeauty.fooddeliveryadmin.screen.menulist.categorylist.CategoryListFragment
import com.bunbeauty.presentation.feature.gallery.selectphoto.SelectPhoto
import com.bunbeauty.presentation.feature.gallery.selectphoto.SelectPhotoViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectPhotoFragment :
    BaseComposeFragment<SelectPhoto.DataState, SelectPhotoViewState, SelectPhoto.Action, SelectPhoto.Event>() {

    override val viewModel: SelectPhotoViewModel by viewModels()

    companion object {
        const val SELECT_PHOTO_REQUEST_KEY = "SELECT_PHOTO_REQUEST_KEY"
        const val SELECTED_PHOTO_KEY = "SELECTED_PHOTO_KEY"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.onAction(SelectPhoto.Action.Init)
    }

    @Composable
    override fun Screen(state: SelectPhotoViewState, onAction: (SelectPhoto.Action) -> Unit) {
        SelectPhotoSuccess(state = state, onAction = onAction)
    }

    @Composable
    fun SelectPhotoSuccess(state: SelectPhotoViewState, onAction: (SelectPhoto.Action) -> Unit) {
        AdminScaffold(
            title = stringResource(R.string.title_select_photo),
            backActionClick = {
                onAction(SelectPhoto.Action.Back)
            },
            actionButton = {
                MainButton(
                    textStringId = R.string.action_select_photo_save,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    onClick = {
                        onAction(SelectPhoto.Action.OnSavePhotoClick)
                    }
                )
            }
        ) {
            AsyncImage(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp)
                    .fillMaxWidth()
                    .clip(AdminCardDefaults.cardShape),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(state.photoLink)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.default_product),
                contentDescription = stringResource(R.string.description_product),
                contentScale = ContentScale.FillWidth
            )
        }
    }

    @Composable
    override fun mapState(state: SelectPhoto.DataState): SelectPhotoViewState {
        return SelectPhotoViewState(
            photoLink = state.photoUrl,
            isLoading = state.isLoading,
            hasError = state.hasError
        )
    }

    override fun handleEvent(event: SelectPhoto.Event) {
        when (event) {
            SelectPhoto.Event.Back -> {
                findNavController().popBackStack()
            }

            is SelectPhoto.Event.Saved -> {
                //todo add toast
                setFragmentResult(
                    SELECT_PHOTO_REQUEST_KEY,
                    bundleOf(SELECTED_PHOTO_KEY to event.photoUrl)
                )
                findNavController().popBackStack(
                    destinationId = R.id.addMenuProductFragment,
                    inclusive = false
                )
            }
        }
    }

    @Preview(showSystemUi = true)
    @Composable
    fun SelectPhotoSuccessPreview() {
        AdminTheme {
            SelectPhotoSuccess(
                state = SelectPhotoViewState(
                    photoLink = "https://vk.com/xidealo?z=photo48589095_457256794%2Falbum48589095_0%2Frev",
                    isLoading = false,
                    hasError = false
                ),
                onAction = {}
            )
        }
    }
}
