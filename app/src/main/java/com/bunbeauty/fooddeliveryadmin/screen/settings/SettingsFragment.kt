package com.bunbeauty.fooddeliveryadmin.screen.settings

import android.os.Bundle
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.fragment.findNavController
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.compose.AdminScaffold
import com.bunbeauty.fooddeliveryadmin.compose.element.bottomsheet.AdminModalBottomSheet
import com.bunbeauty.fooddeliveryadmin.compose.element.button.LoadingButton
import com.bunbeauty.fooddeliveryadmin.compose.element.button.MainButton
import com.bunbeauty.fooddeliveryadmin.compose.element.button.RadioButton
import com.bunbeauty.fooddeliveryadmin.compose.element.button.SecondaryButton
import com.bunbeauty.fooddeliveryadmin.compose.element.card.AdminCard
import com.bunbeauty.fooddeliveryadmin.compose.element.card.AdminCardDefaults
import com.bunbeauty.fooddeliveryadmin.compose.element.card.AdminCardDefaults.noCornerCardShape
import com.bunbeauty.fooddeliveryadmin.compose.element.card.SwitcherCard
import com.bunbeauty.fooddeliveryadmin.compose.element.topbar.AdminHorizontalDivider
import com.bunbeauty.fooddeliveryadmin.compose.screen.ErrorScreen
import com.bunbeauty.fooddeliveryadmin.compose.screen.LoadingScreen
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme
import com.bunbeauty.fooddeliveryadmin.compose.theme.bold
import com.bunbeauty.fooddeliveryadmin.coreui.BaseComposeFragment
import com.bunbeauty.fooddeliveryadmin.main.MessageHost
import com.bunbeauty.fooddeliveryadmin.screen.settings.SettingsViewState.State
import com.bunbeauty.fooddeliveryadmin.screen.settings.SettingsViewState.WorkType
import com.bunbeauty.presentation.feature.settings.SettingsViewModel
import com.bunbeauty.presentation.feature.settings.state.SettingsState
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment :
    BaseComposeFragment<SettingsState.DataState, SettingsViewState, SettingsState.Action, SettingsState.Event>() {

    override val viewModel: SettingsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.onAction(SettingsState.Action.Init)
    }

    @Composable
    override fun Screen(
        state: SettingsViewState,
        onAction: (SettingsState.Action) -> Unit
    ) {
        SettingsScreen(
            state = state,
            onAction = onAction
        )
    }

    @Composable
    override fun mapState(state: SettingsState.DataState): SettingsViewState {
        return state.toViewState()
    }

    @Composable
    private fun SettingsScreen(
        state: SettingsViewState,
        onAction: (SettingsState.Action) -> Unit
    ) {
        AdminScaffold(
            title = stringResource(R.string.title_settings),
            backActionClick = {
                onAction(SettingsState.Action.OnBackClicked)
            },
            backgroundColor = AdminTheme.colors.main.background
        ) {
            when (state.state) {
                State.Loading -> {
                    LoadingScreen()
                }

                State.Error -> {
                    ErrorScreen(
                        mainTextId = R.string.title_common_can_not_load_data,
                        extraTextId = R.string.msg_common_check_connection_and_retry,
                        onClick = {
                            onAction(SettingsState.Action.Init)
                        }
                    )
                }

                is State.Success -> {
                    SuccessSettingsScreen(
                        state = state.state,
                        onAction = onAction
                    )
                }
            }
        }
    }

    @Composable
    private fun SuccessSettingsScreen(
        state: State.Success,
        onAction: (SettingsState.Action) -> Unit,
        modifier: Modifier = Modifier,
        colors: CardColors = AdminCardDefaults.cardColors
    ) {
        Column(
            modifier = modifier.fillMaxSize()
        ) {
            SwitcherCard(
                modifier = Modifier.padding(vertical = 16.dp),
                elevated = false,

                text = stringResource(R.string.msg_settings_unlimited_notifications),
                checked = state.isNotifications,
                onCheckChanged = { isUnlimitedNotifications ->
                    onAction(
                        SettingsState.Action.OnNotificationsClicked(
                            isUnlimitedNotifications = isUnlimitedNotifications
                        )
                    )
                }
            )

            WorkTypeScreen(
                state = state,
                onAction = onAction
            )

            WorkLoadScreen(
                state = state,
                onAction = onAction,
                colors = colors
            )

            //вынест и
            AdminModalBottomSheet(
                title = stringResource(state.acceptOrdersConfirmation.titleResId),
                isShown = state.acceptOrdersConfirmation.isShown,
                onDismissRequest = {
                    onAction(SettingsState.Action.CancelAcceptOrders)
                }
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(state.acceptOrdersConfirmation.descriptionResId),
                        style = AdminTheme.typography.bodyMedium,
                        color = AdminTheme.colors.main.onSurface,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    MainButton(
                        modifier = Modifier.padding(top = 16.dp),
                        text = stringResource(state.acceptOrdersConfirmation.buttonResId),
                        onClick = {
                            onAction(SettingsState.Action.ConfirmNotAcceptOrders)
                        }
                    )
                    SecondaryButton(
                        modifier = Modifier.padding(top = 8.dp),
                        textStringId = R.string.action_common_cancel,
                        onClick = {
                            onAction(SettingsState.Action.CancelAcceptOrders)
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            LoadingButton(
                modifier = Modifier.padding(16.dp),
                text = stringResource(R.string.action_edit_addition_save),
                isLoading = state.isLoading,
                onClick = {
                    onAction(SettingsState.Action.OnSaveSettingsClick)
                }
            )
        }
    }

    @Composable
    private fun WorkTypeScreen(
        state: State.Success,
        onAction: (SettingsState.Action) -> Unit,
        modifier: Modifier = Modifier
    ) {
        Column(modifier = modifier) {
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = stringResource(R.string.msg_settings_type_work),
                style = AdminTheme.typography.titleMedium.bold
            )
            SettingsTypeRow(
                iconRes = R.drawable.ic_delivery,
                textRes = R.string.msg_settings_status_delivery,
                isSelected = state.workType == WorkType.DELIVERY,
                onClick = {
                    onAction(
                        SettingsState.Action.OnSelectStatusClicked(
                            SettingsState.DataState.WorkType.DELIVERY
                        )
                    )
                }
            )
            AdminHorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

            SettingsTypeRow(
                iconRes = R.drawable.ic_pickup,
                textRes = R.string.msg_settings_status_pickup,
                isSelected = state.workType == WorkType.PICKUP,
                onClick = {
                    onAction(
                        SettingsState.Action.OnSelectStatusClicked(
                            SettingsState.DataState.WorkType.PICKUP
                        )
                    )
                }
            )
            AdminHorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

            SettingsTypeRow(
                iconRes = R.drawable.ic_delivery_and_pickup,
                textRes = R.string.msg_settings_status_pickup_delivery,
                isSelected = state.workType == WorkType.DELIVERY_AND_PICKUP,
                onClick = {
                    onAction(
                        SettingsState.Action.OnSelectStatusClicked(
                            SettingsState.DataState.WorkType.DELIVERY_AND_PICKUP
                        )
                    )
                }
            )
            AdminHorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

            SettingsTypeRow(
                iconRes = R.drawable.ic_close_cafe,
                textRes = R.string.msg_settings_status_accept_orders,
                isSelected = state.workType == WorkType.CLOSED,
                onClick = {
                    onAction(
                        SettingsState.Action.OnSelectStatusClicked(
                            SettingsState.DataState.WorkType.CLOSED
                        )
                    )
                }
            )
        }
    }

    @Composable
    private fun WorkLoadScreen(
        state: State.Success,
        onAction: (SettingsState.Action) -> Unit,
        colors: CardColors = AdminCardDefaults.cardColors
    ) {
        Card(
            modifier = Modifier.padding(top = 16.dp),
            colors = colors
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = stringResource(R.string.action_work_load_cafe_disable),
                style = AdminTheme.typography.titleMedium.bold
            )
            Card(
                modifier = Modifier.padding(top = 8.dp),
                colors = colors
            ) {
                Column {
                    SettingsTypeRow(
                        iconRes = R.drawable.ic_cellular_low,
                        textRes = R.string.msg_work_load_low,
                        isSelected = state.workLoad == SettingsViewState.WorkLoad.LOW,
                        onClick = {
                            onAction(
                                SettingsState.Action.OnSelectWorkLoadClicked(
                                    SettingsState.DataState.WorkLoad.LOW
                                )
                            )
                        }
                    )
                    AdminHorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

                    SettingsTypeRow(
                        iconRes = R.drawable.ic_cellular_average,
                        textRes = R.string.msg_work_load_average,
                        isSelected = state.workLoad == SettingsViewState.WorkLoad.AVERAGE,
                        onClick = {
                            onAction(
                                SettingsState.Action.OnSelectWorkLoadClicked(
                                    SettingsState.DataState.WorkLoad.AVERAGE
                                )
                            )
                        }
                    )
                    AdminHorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

                    SettingsTypeRow(
                        iconRes = R.drawable.ic_cellular_high,
                        textRes = R.string.msg_work_load_high,
                        isSelected = state.workLoad == SettingsViewState.WorkLoad.HIGH,
                        onClick = {
                            onAction(
                                SettingsState.Action.OnSelectWorkLoadClicked(
                                    SettingsState.DataState.WorkLoad.HIGH
                                )
                            )
                        }
                    )
                }
            }
        }
    }

    @Composable
    private fun SettingsTypeRow(
        iconRes: Int,
        textRes: Int,
        isSelected: Boolean,
        onClick: () -> Unit,
        modifier: Modifier = Modifier
    ) {
        AdminCard(
            modifier = modifier.fillMaxWidth(),
            onClick = onClick,
            elevated = false,
            shape = noCornerCardShape
        ) {
            Row(
                modifier = modifier
                    .padding(horizontal = 16.dp)
                    .padding(vertical = 16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .size(24.dp),
                    painter = painterResource(iconRes),
                    tint = AdminTheme.colors.main.onSurfaceVariant,
                    contentDescription = null
                )
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp),
                    text = stringResource(textRes),
                    style = AdminTheme.typography.bodyLarge,
                    color = AdminTheme.colors.main.onSurface
                )
                RadioButton(
                    selected = isSelected,
                    onClick = onClick
                )
            }
        }
    }

    override fun handleEvent(event: SettingsState.Event) {
        when (event) {
            SettingsState.Event.GoBackEvent -> {
                findNavController().navigateUp()
            }

            SettingsState.Event.ShowSaveSettingEvent -> {
                (activity as? MessageHost)?.showInfoMessage(
                    resources.getString(R.string.action_settings_save)
                )
                findNavController().popBackStack()
            }

            is SettingsState.Event.ShowErrorMessage -> {
                (activity as? MessageHost)?.showErrorMessage(
                    resources.getString(event.messageId)
                )
            }
        }
    }

    @Preview(showSystemUi = true)
    @Composable
    private fun SettingsScreenPreview() {
        AdminTheme {
            SettingsScreen(
                state = SettingsViewState(
                    state = State.Success(
                        isNotifications = true,
                        workType = WorkType.DELIVERY_AND_PICKUP,
                        acceptOrdersConfirmation = SettingsViewState.AcceptOrdersConfirmation(
                            isShown = false,
                            titleResId = 0,
                            descriptionResId = 0,
                            buttonResId = 0
                        ),
                        isLoading = false,
                        workLoad = SettingsViewState.WorkLoad.LOW
                    )
                ),
                onAction = {}
            )
        }
    }
}
