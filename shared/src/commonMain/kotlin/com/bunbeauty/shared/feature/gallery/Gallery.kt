package com.bunbeauty.shared.feature.gallery

import com.bunbeauty.domain.model.Photo
import com.bunbeauty.shared.viewmodel.base.BaseAction
import com.bunbeauty.shared.viewmodel.base.BaseDataState
import com.bunbeauty.shared.viewmodel.base.BaseEvent

interface Gallery {
    data class DataState(
        val photoList: List<Photo>,
        val isLoading: Boolean,
        val isRefreshing: Boolean,
        val hasError: Boolean,
    ) : BaseDataState

    sealed interface Action : BaseAction {
        data object Init : Action

        data object Refresh : Action

        data object Back : Action

        data class OnSelectedPhotoClick(
            val photoUrl: String,
        ) : Action
    }

    sealed interface Event : BaseEvent {
        data object Back : Event
    }
}
