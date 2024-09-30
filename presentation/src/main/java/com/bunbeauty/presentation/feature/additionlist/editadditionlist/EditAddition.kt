package com.bunbeauty.presentation.feature.additionlist.editadditionlist

import com.bunbeauty.presentation.viewmodel.base.BaseAction
import com.bunbeauty.presentation.viewmodel.base.BaseDataState
import com.bunbeauty.presentation.viewmodel.base.BaseEvent

interface EditAddition {
    data class DataState(
        val uuid: String,
        val name: String,
        val hasEditNameError: Boolean,
        val priority: Int,
        val price: String?,
        val hasEditPriceError: Boolean,
        val fullName: String?,
        val isLoading: Boolean,
        val isVisible: Boolean
    ) : BaseDataState

    sealed interface Action : BaseAction {

        data object InitAddition : Action
        data class EditNameAddition(val name: String) : Action
        data class EditPriorityAddition(val priority: String) : Action
        data class EditFullNameAddition(val fullName: String?) : Action
        data class EditPriceAddition(val price: String?) : Action
        data object OnSaveEditAdditionClick : Action
        data class OnVisibleClick(val isVisible: Boolean) : Action
        data object OnBackClick : Action
    }

    sealed interface Event : BaseEvent {
        data object Back : Event
        data class ShowUpdateAdditionSuccess(val additionName: String) : Event
    }
}
