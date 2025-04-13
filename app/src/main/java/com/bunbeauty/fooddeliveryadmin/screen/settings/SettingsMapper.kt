package com.bunbeauty.fooddeliveryadmin.screen.settings

import androidx.compose.runtime.Composable
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.presentation.feature.settings.state.SettingsState

@Composable
internal fun SettingsState.DataState.toViewState(): SettingsViewState {
    return SettingsViewState(
        state = when (state) {
            SettingsState.DataState.State.LOADING -> SettingsViewState.State.Loading
            SettingsState.DataState.State.ERROR -> SettingsViewState.State.Error
            SettingsState.DataState.State.SUCCESS -> {
                SettingsViewState.State.Success(
                    isNotifications = isUnlimitedNotifications,
                    workType = workType.toViewStateWorkType(),
                    workLoad = workLoad.toViewStateWorkLoad(),
                    acceptOrdersConfirmation = SettingsViewState.AcceptOrdersConfirmation(
                        isShown = showAcceptOrdersConfirmation,
                        titleResId = R.string.title_settings_disable_orders,
                        descriptionResId = R.string.msg_settings_disable_orders,
                        buttonResId = R.string.action_settings_disable
                    ),
                    isLoading = isLoading
                )
            }
        }
    )
}

fun SettingsState.DataState.WorkType.toViewStateWorkType(): SettingsViewState.WorkType {
    return when (this) {
        SettingsState.DataState.WorkType.DELIVERY -> SettingsViewState.WorkType.DELIVERY
        SettingsState.DataState.WorkType.PICKUP -> SettingsViewState.WorkType.PICKUP
        SettingsState.DataState.WorkType.DELIVERY_AND_PICKUP -> SettingsViewState.WorkType.DELIVERY_AND_PICKUP
        SettingsState.DataState.WorkType.CLOSED -> SettingsViewState.WorkType.CLOSED
    }
}

fun SettingsState.DataState.WorkLoad.toViewStateWorkLoad(): SettingsViewState.WorkLoad {
    return when (this) {
        SettingsState.DataState.WorkLoad.LOW -> SettingsViewState.WorkLoad.LOW
        SettingsState.DataState.WorkLoad.AVERAGE -> SettingsViewState.WorkLoad.AVERAGE
        SettingsState.DataState.WorkLoad.HIGH -> SettingsViewState.WorkLoad.HIGH
    }
}
