package com.bunbeauty.shared.feature.gallery

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.feature.gallery.FetchPhotoListUseCase
import com.bunbeauty.domain.feature.gallery.GetPhotoListUseCase
import com.bunbeauty.shared.extension.launchSafe
import com.bunbeauty.shared.viewmodel.base.BaseStateViewModel

class GalleryViewModel(
    private val getPhotoListUseCase: GetPhotoListUseCase,
    private val fetchPhotoListUseCase: FetchPhotoListUseCase,
) : BaseStateViewModel<Gallery.DataState, Gallery.Action, Gallery.Event>(
        initState =
            Gallery.DataState(
                photoList = listOf(),
                isLoading = true,
                hasError = false,
                isRefreshing = false,
            ),
    ) {
    override fun reduce(
        action: Gallery.Action,
        dataState: Gallery.DataState,
    ) {
        when (action) {
            Gallery.Action.Init -> loadData()
            Gallery.Action.Refresh -> refreshData()
            Gallery.Action.Back -> sendEvent { Gallery.Event.Back }
            is Gallery.Action.OnSelectedPhotoClick -> {
                // TODO not implemented
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
                        hasError = false,
                    )
                }
            },
            onError = {
                setState {
                    copy(
                        hasError = true,
                        isLoading = false,
                    )
                }
            },
        )
    }

    private fun refreshData() {
        setState {
            copy(
                isRefreshing = true,
            )
        }
        viewModelScope.launchSafe(
            block = {
                setState {
                    copy(
                        photoList = fetchPhotoListUseCase(),
                        isLoading = false,
                        isRefreshing = false,
                        hasError = false,
                    )
                }
            },
            onError = {
                setState {
                    copy(
                        hasError = true,
                        isRefreshing = false,
                    )
                }
            },
        )
    }
}
