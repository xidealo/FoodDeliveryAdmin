package com.bunbeauty.presentation.feature.menulist.cropimage

import com.bunbeauty.presentation.viewmodel.base.BaseAction
import com.bunbeauty.presentation.viewmodel.base.BaseDataState
import com.bunbeauty.presentation.viewmodel.base.BaseEvent

interface CropImage {

    data class DataState(
        val isLoading: Boolean,
        val uri: String?
    ): BaseDataState

    sealed interface Action: BaseAction {
        data class SetImageUrl(val uri: String): Action
        data object SaveClick: Action
    }

    sealed interface Event: BaseEvent {
        data object CropImage: Event
    }

}