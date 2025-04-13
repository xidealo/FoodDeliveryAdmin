package com.bunbeauty.fooddeliveryadmin.screen.settings

import androidx.compose.runtime.Composable
import com.bunbeauty.domain.model.settings.WorkLoad
import com.bunbeauty.domain.model.settings.WorkType
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

fun WorkType.toViewStateWorkType(): SettingsViewState.WorkType {
    return when (this) {
        WorkType.DELIVERY -> SettingsViewState.WorkType.DELIVERY
        WorkType.PICKUP -> SettingsViewState.WorkType.PICKUP
        WorkType.DELIVERY_AND_PICKUP -> SettingsViewState.WorkType.DELIVERY_AND_PICKUP
        WorkType.CLOSED -> SettingsViewState.WorkType.CLOSED
    }
}

fun WorkLoad.toViewStateWorkLoad(): SettingsViewState.WorkLoad {
    return when (this) {
        WorkLoad.LOW -> SettingsViewState.WorkLoad.LOW
        WorkLoad.AVERAGE -> SettingsViewState.WorkLoad.AVERAGE
        WorkLoad.HIGH -> SettingsViewState.WorkLoad.HIGH
    }
}
