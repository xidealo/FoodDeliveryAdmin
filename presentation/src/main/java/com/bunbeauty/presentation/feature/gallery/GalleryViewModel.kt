package com.bunbeauty.presentation.feature.gallery

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.feature.gallery.FetchPhotoListUseCase
import com.bunbeauty.domain.feature.gallery.GetPhotoListUseCase
import com.bunbeauty.presentation.extension.launchSafe
import com.bunbeauty.presentation.viewmodel.base.BaseStateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val getPhotoListUseCase: GetPhotoListUseCase,
    private val fetchPhotoListUseCase: FetchPhotoListUseCase
) : BaseStateViewModel<Gallery.DataState, Gallery.Action, Gallery.Event>(
    initState = Gallery.DataState(
        photoList = listOf(),
        isLoading = true,
        hasError = false,
        isRefreshing = false
    )
) {

    override fun reduce(action: Gallery.Action, dataState: Gallery.DataState) {
        when (action) {
            Gallery.Action.Init -> loadData()
            Gallery.Action.Refresh -> refreshData()
            Gallery.Action.Back -> addEvent { Gallery.Event.Back }
            is Gallery.Action.OnSelectedPhotoClick -> addEvent {
                Gallery.Event.SelectPhoto(action.photoUrl)
            }
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
                        hasError = true,
                        isLoading = false
                    )
                }
            }
        )
    }

    private fun refreshData() {
        setState {
            copy(
                isRefreshing = true
            )
        }
        viewModelScope.launchSafe(
            block = {
                setState {
                    copy(
                        photoList = fetchPhotoListUseCase(),
                        isLoading = false,
                        isRefreshing = false,
                        hasError = false
                    )
                }
            },
            onError = {
                setState {
                    copy(
                        hasError = true,
                        isRefreshing = false
                    )
                }
            }
        )
    }
}
