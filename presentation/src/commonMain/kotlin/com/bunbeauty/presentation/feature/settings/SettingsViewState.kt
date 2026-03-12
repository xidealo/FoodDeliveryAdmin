package com.bunbeauty.presentation.feature.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import com.bunbeauty.domain.model.settings.WorkLoad
import com.bunbeauty.domain.model.settings.WorkType
import com.bunbeauty.presentation.feature.settings.state.SettingsState
import com.bunbeauty.presentation.viewmodel.base.BaseViewState
import fooddeliveryadmin.presentation.generated.resources.Res
import fooddeliveryadmin.presentation.generated.resources.action_settings_disable
import fooddeliveryadmin.presentation.generated.resources.msg_settings_disable_orders
import fooddeliveryadmin.presentation.generated.resources.title_settings_disable_orders
import org.jetbrains.compose.resources.StringResource

@Immutable
data class SettingsViewState(
    val state: State,
) : BaseViewState {
    @Immutable
    sealed interface State {
        data object Loading : State

        data object Error : State

        data class Success(
            val isNotifications: Boolean,
            val isAppliances: Boolean,
            val workType: WorkType,
            val acceptOrdersConfirmation: AcceptOrdersConfirmation,
            val isLoading: Boolean,
            val workLoad: WorkLoad,
        ) : State
    }

    @Immutable
    data class AcceptOrdersConfirmation(
        val isShown: Boolean,
        val titleStringId: StringResource,
        val descriptionStringId: StringResource,
        val buttonStringId: StringResource,
    )
}

@Composable
internal fun SettingsState.DataState.toViewState(): SettingsViewState =
    SettingsViewState(
        state =
            when (state) {
                SettingsState.DataState.State.LOADING -> SettingsViewState.State.Loading
                SettingsState.DataState.State.ERROR -> SettingsViewState.State.Error
                SettingsState.DataState.State.SUCCESS -> {
                    SettingsViewState.State.Success(
                        isNotifications = isUnlimitedNotifications,
                        isAppliances = isKitchenAppliances,
                        workType = workType,
                        workLoad = workLoad,
                        acceptOrdersConfirmation =
                            SettingsViewState.AcceptOrdersConfirmation(
                                isShown = showAcceptOrdersConfirmation,
                                titleStringId = Res.string.title_settings_disable_orders,
                                descriptionStringId = Res.string.msg_settings_disable_orders,
                                buttonStringId = Res.string.action_settings_disable,
                            ),
                        isLoading = isLoading,
                    )
                }
            },
    )
