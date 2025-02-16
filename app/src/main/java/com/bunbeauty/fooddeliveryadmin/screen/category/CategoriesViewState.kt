package com.bunbeauty.fooddeliveryadmin.screen.category

import androidx.compose.runtime.Immutable
import com.bunbeauty.presentation.viewmodel.base.BaseViewState

@Immutable
data class CategoriesViewState(val state: State) : BaseViewState {
    @Immutable
    sealed interface State {
        data object Loading : State
        data object Error : State
        data object Success : State
    }
}