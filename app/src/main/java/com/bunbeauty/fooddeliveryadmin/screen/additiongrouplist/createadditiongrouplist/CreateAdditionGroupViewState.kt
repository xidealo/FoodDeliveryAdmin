package com.bunbeauty.fooddeliveryadmin.screen.additiongrouplist.createadditiongrouplist

import androidx.compose.runtime.Immutable
import com.bunbeauty.fooddeliveryadmin.screen.menulist.common.TextFieldUi
import com.bunbeauty.presentation.viewmodel.base.BaseViewState

@Immutable
data class CreateAdditionGroupViewState(
    val state: State
) : BaseViewState {

    @Immutable
    sealed interface State {
        data object Loading : State
        data object Error : State
        data class Success(
            val isLoading: Boolean,
            val nameField: TextFieldUi,
            val singleChoice: Boolean,
            val isShowMenuVisible: Boolean
        ) : State
    }
}
