package com.bunbeauty.presentation.feature.additiongrouplist.createadditiondrouplist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bunbeauty.presentation.designsystem.compose.AdminScaffold
import com.bunbeauty.presentation.designsystem.compose.element.button.LoadingButton
import com.bunbeauty.presentation.designsystem.compose.element.card.SwitcherCard
import com.bunbeauty.presentation.designsystem.compose.element.textfield.AdminTextField
import com.bunbeauty.presentation.designsystem.compose.element.topbar.AdminHorizontalDivider
import com.bunbeauty.presentation.designsystem.compose.screen.ErrorScreen
import com.bunbeauty.presentation.designsystem.compose.screen.LoadingScreen
import com.bunbeauty.presentation.designsystem.compose.theme.AdminTheme
import fooddeliveryadmin.presentation.generated.resources.Res
import fooddeliveryadmin.presentation.generated.resources.action_addition_list_group_show_in_menu
import fooddeliveryadmin.presentation.generated.resources.action_create_category_save
import fooddeliveryadmin.presentation.generated.resources.hint_edit_create_addition_group_name
import fooddeliveryadmin.presentation.generated.resources.msg_addition_list_group_appliances_hint
import fooddeliveryadmin.presentation.generated.resources.msg_addition_list_group_switcher_card
import fooddeliveryadmin.presentation.generated.resources.msg_common_check_connection_and_retry
import fooddeliveryadmin.presentation.generated.resources.msg_create_addition_group_created
import fooddeliveryadmin.presentation.generated.resources.title_common_can_not_load_data
import fooddeliveryadmin.presentation.generated.resources.title_create_addition_group
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CreateAdditionGroupRouteScreen(
    viewModel: CreateAdditionGroupViewModel = koinViewModel(),
    showInfoMessage: (String, Int) -> Unit,
    goBack: () -> Unit,
) {
    val viewState by viewModel.state.collectAsStateWithLifecycle()
    val onAction =
        remember {
            { event: CreateAdditionGroupDataState.Action ->
                viewModel.onAction(event)
            }
        }

    val effects by viewModel.events.collectAsStateWithLifecycle()
    val consumeEffects =
        remember {
            {
                viewModel.consumeEvents(effects)
            }
        }

    CreateAdditionGroupEffect(
        effects = effects,
        consumeEffects = consumeEffects,
        showInfoMessage = showInfoMessage,
        goBack = goBack,
    )

    CreateAdditionGroupScreen(
        state = viewState.toViewState(),
        onAction = onAction,
        goBack = goBack,
    )
}

@Composable
private fun CreateAdditionGroupEffect(
    effects: List<CreateAdditionGroupDataState.Event>,
    showInfoMessage: (String, Int) -> Unit,
    goBack: () -> Unit,
    consumeEffects: () -> Unit,
) {
    LaunchedEffect(effects) {
        effects.forEach { effect ->
            when (effect) {
                CreateAdditionGroupDataState.Event.GoBackEvent -> {
                    goBack()
                }

                is CreateAdditionGroupDataState.Event.ShowUpdateAdditionGroupSuccess -> {
                    showInfoMessage(
                        getString(
                            Res.string.msg_create_addition_group_created,
                            effect.additionGroupName,
                        ),
                        0
                    )
                    goBack()
                }
            }
        }
        consumeEffects()
    }
}

@Composable
private fun CreateAdditionGroupScreen(
    state: CreateAdditionGroupViewState,
    onAction: (CreateAdditionGroupDataState.Action) -> Unit,
    goBack: () -> Unit,
) {
    AdminScaffold(
        title = stringResource(Res.string.title_create_addition_group),
        backgroundColor = AdminTheme.colors.main.surface,
        pullRefreshEnabled = true,
        backActionClick = {
            onAction(CreateAdditionGroupDataState.Action.OnBackClick)
        },
    ) {
        when (state.state) {
            CreateAdditionGroupViewState.State.Error ->
                ErrorScreen(
                    mainTextId = Res.string.title_common_can_not_load_data,
                    extraTextId = Res.string.msg_common_check_connection_and_retry,
                    onClick = {
                        onAction(CreateAdditionGroupDataState.Action.OnErrorStateClicked)
                    },
                )

            CreateAdditionGroupViewState.State.Loading -> LoadingScreen()

            is CreateAdditionGroupViewState.State.Success ->
                CreateAdditionGroupScreenSuccess(
                    state = state.state,
                    onAction = onAction,
                )
        }
    }
}

@Composable
private fun CreateAdditionGroupScreenSuccess(
    state: CreateAdditionGroupViewState.State.Success,
    onAction: (CreateAdditionGroupDataState.Action) -> Unit,
) {
    Column {
        AdminTextField(
            modifier = Modifier.padding(16.dp),
            labelText = stringResource(Res.string.hint_edit_create_addition_group_name),
            value = state.nameField.value,
            onValueChange = { name ->
                onAction(
                    CreateAdditionGroupDataState.Action.CreateNameAdditionGroupChanged(
                        name,
                    ),
                )
            },
            errorText = state.nameField.errorResId,
            isError = state.nameField.isError,
            enabled = !state.isLoading,
        )
        SwitcherCard(
            elevated = false,
            text = stringResource(Res.string.action_addition_list_group_show_in_menu),
            checked = state.isShowMenuVisible,
            onCheckChanged = { isVisible ->
                onAction(
                    CreateAdditionGroupDataState.Action.OnVisibleClick,
                )
            },
        )
        AdminHorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

        SwitcherCard(
            hint = stringResource(Res.string.msg_addition_list_group_appliances_hint),
            elevated = false,
            text = stringResource(Res.string.msg_addition_list_group_switcher_card),
            checked = state.singleChoice,
            onCheckChanged = { isOneVisible ->
                onAction(
                    CreateAdditionGroupDataState.Action.OnOneAdditionVisibleClick,
                )
            },
        )
        AdminHorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

        Spacer(modifier = Modifier.weight(1f))
        LoadingButton(
            modifier = Modifier.padding(16.dp),
            text = stringResource(Res.string.action_create_category_save),
            isLoading = state.isLoading,
            onClick = {
                onAction(CreateAdditionGroupDataState.Action.OnSaveAdditionGroupClick)
            },
        )
    }
}
