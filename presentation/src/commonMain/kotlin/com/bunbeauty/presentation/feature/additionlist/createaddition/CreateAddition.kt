package com.bunbeauty.presentation.feature.additionlist.createaddition

import com.bunbeauty.presentation.feature.image.ImageFieldData
import com.bunbeauty.presentation.viewmodel.base.BaseAction
import com.bunbeauty.presentation.viewmodel.base.BaseDataState
import com.bunbeauty.presentation.viewmodel.base.BaseEvent

interface CreateAddition {
    data class DataState(
        val uuid: String,
        val name: String,
        val hasEditNameError: Boolean,
        val price: String,
        val tag: String,
        val fullName: String,
        val isLoading: Boolean,
        val isVisible: Boolean,
        val imageField: ImageFieldData,
    ) : BaseDataState

    sealed interface Action : BaseAction {
        data class EditNameAddition(
            val name: String,
        ) : Action

        data class EditFullNameAddition(
            val fullName: String,
        ) : Action

        data class EditPriceAddition(
            val price: String,
        ) : Action

        data class EditTagAddition(
            val tag: String,
        ) : Action

        data object OnSaveCreateAdditionClick : Action

        data class OnVisibleClick(
            val isVisible: Boolean,
        ) : Action

        data object OnBackClick : Action

        data class SetImage(
            val croppedImageUri: String,
        ) : Action
    }

    sealed interface Event : BaseEvent {
        data object Back : Event

        data class ShowCreatedAdditionSuccess(
            val additionName: String,
        ) : Event
    }
}
