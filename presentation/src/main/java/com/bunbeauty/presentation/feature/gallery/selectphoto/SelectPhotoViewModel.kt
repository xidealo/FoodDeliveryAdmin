package com.bunbeauty.presentation.feature.gallery.selectphoto

import androidx.lifecycle.SavedStateHandle
import com.bunbeauty.presentation.viewmodel.base.BaseStateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

private const val SELECTED_PHOTO_URL = "selectedPhotoUrl"

@HiltViewModel
class SelectPhotoViewModel @Inject constructor(
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

            SelectPhoto.Action.Back -> addEvent {
                SelectPhoto.Event.Back
            }

            SelectPhoto.Action.OnSavePhotoClick -> addEvent {
                SelectPhoto.Event.Saved(
                    photoUrl = dataState.photoUrl ?: ""
                )
            }
        }
    }
}
