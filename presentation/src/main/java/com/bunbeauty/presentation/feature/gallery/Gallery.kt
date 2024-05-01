package com.bunbeauty.presentation.feature.gallery

import com.bunbeauty.presentation.viewmodel.base.BaseAction
import com.bunbeauty.presentation.viewmodel.base.BaseEvent
import com.bunbeauty.presentation.viewmodel.base.BaseViewDataState

interface Gallery {
    data class ViewDataState(
        val photoList: List<String>,
    ) : BaseViewDataState

    sealed interface Action : BaseAction {
        data object Init : Action
    }

    sealed interface Event : BaseEvent {
        data object Back : Event
    }
}