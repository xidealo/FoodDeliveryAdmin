package com.bunbeauty.presentation.feature.gallery

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.feature.gallery.GetPhotoListUseCase
import com.bunbeauty.presentation.extension.launchSafe
import com.bunbeauty.presentation.viewmodel.base.BaseStateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val getPhotoListUseCase: GetPhotoListUseCase
) : BaseStateViewModel<Gallery.ViewDataState, Gallery.Action, Gallery.Event>(
    initState = Gallery.ViewDataState(
        photoList = listOf(),
        isLoading = true,
        hasError = false
    )
) {

    override fun reduce(action: Gallery.Action, dataState: Gallery.ViewDataState) {
        when (action) {
            Gallery.Action.Init -> loadData()
            Gallery.Action.Back -> addEvent { Gallery.Event.Back }
        }
    }

    private fun loadData() {
        viewModelScope.launchSafe(
            block = {
                setState {
                    copy(
                        photoList = getPhotoListUseCase(),
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
