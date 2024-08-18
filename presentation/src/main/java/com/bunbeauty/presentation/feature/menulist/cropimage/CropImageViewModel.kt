package com.bunbeauty.presentation.feature.menulist.cropimage

import com.bunbeauty.presentation.viewmodel.base.BaseStateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CropImageViewModel @Inject constructor() :
    BaseStateViewModel<CropImage.DataState, CropImage.Action, CropImage.Event>(
        initState = CropImage.DataState(
            isLoading = false,
            uri = null
        )
    ) {

    override fun reduce(action: CropImage.Action, dataState: CropImage.DataState) {
        when (action) {
            is CropImage.Action.SetImageUrl -> setState {
                copy(uri = action.uri)
            }

            is CropImage.Action.SaveClick -> {
                setState { copy(isLoading = true) }
                sendEvent {
                    CropImage.Event.CropImage
                }
            }
        }
    }
}
