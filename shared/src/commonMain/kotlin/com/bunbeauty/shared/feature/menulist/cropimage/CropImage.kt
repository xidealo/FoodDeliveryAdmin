package com.bunbeauty.shared.feature.menulist.cropimage

import com.bunbeauty.shared.viewmodel.base.BaseAction
import com.bunbeauty.shared.viewmodel.base.BaseDataState
import com.bunbeauty.shared.viewmodel.base.BaseEvent

interface CropImage {
    data class DataState(
        val isLoading: Boolean,
        val uri: String?,
    ) : BaseDataState

    sealed interface Action : BaseAction {
        data object BackClick : Action

        data class SetImageUrl(
            val uri: String,
        ) : Action

        data object SaveClick : Action
    }

    sealed interface Event : BaseEvent {
        data object GoBack : Event

        data object CropImage : Event
    }
}
