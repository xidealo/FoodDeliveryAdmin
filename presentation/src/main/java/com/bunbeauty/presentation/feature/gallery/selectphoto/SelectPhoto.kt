package com.bunbeauty.presentation.feature.gallery.selectphoto

import com.bunbeauty.presentation.viewmodel.base.BaseAction
import com.bunbeauty.presentation.viewmodel.base.BaseDataState
import com.bunbeauty.presentation.viewmodel.base.BaseEvent

interface SelectPhoto {
    data class DataState(
        val photoUrl: String?,
        val isLoading: Boolean,
        val hasError: Boolean
    ) : BaseDataState

    sealed interface Action : BaseAction {
        data object Init : Action
        data object OnSavePhotoClick : Action
        data object Back : Action
    }

    sealed interface Event : BaseEvent {
        data object Back : Event
        data class Saved(val photoUrl: String) : Event
    }
}
