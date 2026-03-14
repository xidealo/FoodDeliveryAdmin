package com.bunbeauty.presentation.feature.additionlist.editadditionlist.state

import androidx.compose.runtime.Immutable
import com.bunbeauty.presentation.feature.common.TextFieldUi
import com.bunbeauty.presentation.feature.menulist.createmenuproduct.ImageFieldUi
import com.bunbeauty.presentation.viewmodel.base.BaseViewState

@Immutable
data class EditAdditionViewState(
    val state: State,
) : BaseViewState {
    @Immutable
    sealed interface State {
        data object Loading : State

        data object Error : State

        data class Success(
            val nameField: TextFieldUi,
            val fullName: String,
            val price: String,
            val tag: String,
            val isVisible: Boolean,
            val isLoading: Boolean,
            val imageFieldUi: ImageFieldUi,
        ) : State
    }
}
