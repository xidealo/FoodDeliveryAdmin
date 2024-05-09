package com.bunbeauty.presentation.feature.additionlist.editadditionlist

import com.bunbeauty.presentation.viewmodel.base.BaseAction
import com.bunbeauty.presentation.viewmodel.base.BaseDataState
import com.bunbeauty.presentation.viewmodel.base.BaseEvent
import com.bunbeauty.presentation.viewmodel.base.BaseViewDataState

interface EditAddition {
    data class DataState(
        val uuid: String,
        val name: String,
        val hasNameError: Boolean = false,
        val priority: Int?,
        val prise: Int?,
        val fullName: String?,
        val hasFullNameError: Boolean = false,
        val isLoading: Boolean,
        val isVisible: Boolean,
        val error: Throwable? = null
    ) : BaseDataState

    sealed interface Action : BaseAction {

        data object Init : Action
        data object Name : Action
        data object Priority : Action
        data object FullName : Action
        data object Prise : Action
        data object SaveEditAdditionClick : Action
        data class OnVisibleClick(val isVisible: Boolean ) : Action
        data object OnBackClick : Action
    }

    sealed interface Event : BaseEvent {
        data object Back : Event
    }
}
