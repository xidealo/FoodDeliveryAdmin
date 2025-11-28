package com.bunbeauty.fooddeliveryadmin.screen.category.editcategory

import androidx.compose.runtime.Immutable
import com.bunbeauty.fooddeliveryadmin.screen.menulist.common.TextFieldUi
import com.bunbeauty.presentation.viewmodel.base.BaseViewState

data class EditCategoryViewState(
    val state: State
) : BaseViewState {
    @Immutable
    sealed interface State {
        data object Loading : State
        data object Error : State
        data class Success(
            val nameField: TextFieldUi,
            val isLoading: Boolean
        ) : State
    }
}
