package com.bunbeauty.fooddeliveryadmin.screen.profile

import com.bunbeauty.presentation.viewmodel.base.BaseViewState

data class ProfileViewState(
    val state: State
): BaseViewState {

    sealed interface State {
        data object Loading : State
        data object Error : State
        data class Success(
            val role: String,
            val userName: String,
            val acceptOrders: Boolean
        ) : State
    }
}
