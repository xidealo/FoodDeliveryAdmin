package com.bunbeauty.presentation.feature.gallery.selectphoto

import androidx.lifecycle.SavedStateHandle
import com.bunbeauty.presentation.viewmodel.base.BaseStateViewModel

private const val SELECTED_PHOTO_URL = "selectedPhotoUrl"

class SelectPhotoViewModel(
    private val savedStateHandle: SavedStateHandle
) : BaseStateViewModel<SelectPhoto.DataState, SelectPhoto.Action, SelectPhoto.Event>(
    initState = SelectPhoto.DataState(
        photoUrl = null,
        isLoading = true,
        hasError = false
    )
) {

    override fun reduce(action: SelectPhoto.Action, dataState: SelectPhoto.DataState) {
        when (action) {
            SelectPhoto.Action.Init -> setState {
                copy(
                    photoUrl = savedStateHandle.get<String>(SELECTED_PHOTO_URL),
                    isLoading = false,
                    hasError = false
                )
            }

            SelectPhoto.Action.Back -> sendEvent {
                SelectPhoto.Event.Back
            }

            SelectPhoto.Action.OnSavePhotoClick -> sendEvent {
                SelectPhoto.Event.Saved(
                    photoUrl = dataState.photoUrl ?: ""
                )
            }
        }
    }
}
