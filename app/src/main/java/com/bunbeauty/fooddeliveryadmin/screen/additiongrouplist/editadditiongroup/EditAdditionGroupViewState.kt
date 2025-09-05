package com.bunbeauty.fooddeliveryadmin.screen.additiongrouplist.editadditiongroup

import androidx.compose.runtime.Immutable
import com.bunbeauty.fooddeliveryadmin.screen.menulist.common.TextFieldUi
import com.bunbeauty.presentation.viewmodel.base.BaseViewState

data class EditAdditionGroupViewState(
    val state: State
) : BaseViewState {
    @Immutable
    sealed interface State {
        data object Loading : State
        data object Error : State
        data class Success(
            val nameField: TextFieldUi,
            val isLoading: Boolean,
            val isVisible: Boolean,
            val isVisibleSingleChoice: Boolean
        ) : State
    }
}