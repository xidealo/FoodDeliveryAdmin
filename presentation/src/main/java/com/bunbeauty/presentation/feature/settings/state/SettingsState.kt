package com.bunbeauty.presentation.feature.settings.state

import androidx.annotation.StringRes
import com.bunbeauty.domain.model.settings.WorkLoad
import com.bunbeauty.domain.model.settings.WorkType
import com.bunbeauty.presentation.viewmodel.base.BaseAction
import com.bunbeauty.presentation.viewmodel.base.BaseDataState
import com.bunbeauty.presentation.viewmodel.base.BaseEvent

interface SettingsState {

    data class DataState(
        val state: State,
        val isUnlimitedNotifications: Boolean,
        val isKitchenAppliances: Boolean,
        val workType: WorkType,
        val workLoad: WorkLoad,
        val isLoading: Boolean,
        val showAcceptOrdersConfirmation: Boolean
    ) : BaseDataState {

        enum class State {
            LOADING,
            SUCCESS,
            ERROR
        }
    }

    sealed interface Action : BaseAction {
        data object Init : Action
        data object OnBackClicked : Action
        data class OnNotificationsClicked(val isUnlimitedNotifications: Boolean) : Action
        data class OnAppliancesClicked(val isKitchenAppliances: Boolean) : Action
        data object OnSaveSettingsClick : Action
        data class OnSelectStatusClicked(val workType: WorkType) : Action
        data class OnSelectWorkLoadClicked(val workload: WorkLoad) : Action
        data object CancelAcceptOrders : Action
        data object ConfirmNotAcceptOrders : Action
    }

    sealed interface Event : BaseEvent {
        data object GoBackEvent : Event
        data object ShowSaveSettingEvent : Event
        data class ShowErrorMessage(@StringRes val messageId: Int) : Event
    }
}
