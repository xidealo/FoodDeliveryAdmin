package com.bunbeauty.presentation.feature.gallery

import com.bunbeauty.domain.model.Photo
import com.bunbeauty.presentation.viewmodel.base.BaseAction
import com.bunbeauty.presentation.viewmodel.base.BaseDataState
import com.bunbeauty.presentation.viewmodel.base.BaseEvent

interface Gallery {
    data class DataState(
        val photoList: List<Photo>,
        val isLoading: Boolean,
        val hasError: Boolean
    ) : BaseDataState

    sealed interface Action : BaseAction {
        data object Init : Action
        data object Back : Action
    }

    sealed interface Event : BaseEvent {
        data object Back : Event
    }
}
