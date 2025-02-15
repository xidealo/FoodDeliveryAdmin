package com.bunbeauty.fooddeliveryadmin.screen.settings

import androidx.compose.runtime.Immutable
import com.bunbeauty.presentation.viewmodel.base.BaseViewState

@Immutable
data class SettingsViewState(
    val state: State
) : BaseViewState {
    @Immutable
    sealed interface State {
        data object Loading : State
        data object Error : State
        data class Success(
            val isNotifications: Boolean,
            val workType: WorkType,
            val acceptOrdersConfirmation: AcceptOrdersConfirmation,
            val isLoading: Boolean
        ) : State
    }
    enum class WorkType {
        DELIVERY,
        PICKUP,
        DELIVERY_AND_PICKUP,
        CLOSED
    }

    data class AcceptOrdersConfirmation(
        val isShown: Boolean,
        val titleResId: Int,
        val descriptionResId: Int,
        val buttonResId: Int
    )
}
