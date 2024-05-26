package com.bunbeauty.presentation.feature.gallery.selectphoto

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.bunbeauty.presentation.extension.launchSafe
import com.bunbeauty.presentation.viewmodel.base.BaseStateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SelectPhotoViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : BaseStateViewModel<SelectPhoto.DataState, SelectPhoto.Action, SelectPhoto.Event>(
    initState = SelectPhoto.DataState(
        photo = null,
        isLoading = true,
        hasError = false
    )
) {

    override fun reduce(action: SelectPhoto.Action, dataState: SelectPhoto.DataState) {
        when (action) {
            SelectPhoto.Action.Init -> {}
            SelectPhoto.Action.Back -> {}
        }
    }

    private fun loadData() {
        viewModelScope.launchSafe(
            block = {
                setState {
                    copy(
                        photo = "",
                        isLoading = false,
                        hasError = false
                    )
                }
            },
            onError = {
                setState {
                    copy(
                        hasError = true
                    )
                }
            }
        )
    }
}
