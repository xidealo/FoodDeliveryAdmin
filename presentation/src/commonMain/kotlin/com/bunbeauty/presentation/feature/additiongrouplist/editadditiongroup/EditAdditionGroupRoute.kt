package com.bunbeauty.presentation.feature.additiongrouplist.editadditiongroup

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
import fooddeliveryadmin.presentation.generated.resources.action_edit_addition_group_save
import fooddeliveryadmin.presentation.generated.resources.hint_edit_addition_group_name
import fooddeliveryadmin.presentation.generated.resources.msg_common_check_connection_and_retry
import fooddeliveryadmin.presentation.generated.resources.msg_edit_addition_group_is_visible_menu
import fooddeliveryadmin.presentation.generated.resources.msg_edit_addition_group_single_addition_menu
import fooddeliveryadmin.presentation.generated.resources.msg_edit_addition_group_single_сhoice_hint
import fooddeliveryadmin.presentation.generated.resources.msg_edit_addition_group_updated
import fooddeliveryadmin.presentation.generated.resources.title_common_can_not_load_data
import fooddeliveryadmin.presentation.generated.resources.title_edit_addition_group
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun EditAdditionGroupRouteScreen(
    viewModel: EditAdditionGroupViewModel = koinViewModel(),
    showInfoMessage: (String, Int) -> Unit,
    goBack: () -> Unit,
) {
    val viewState by viewModel.state.collectAsStateWithLifecycle()
    val onAction =
        remember {
            { event: EditAdditionGroupDataState.Action ->
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

    LaunchedEffect(Unit) {
        onAction(EditAdditionGroupDataState.Action.Init)
    }

    EditAdditionGroupEffect(
        effects = effects,
        consumeEffects = consumeEffects,
        showInfoMessage = showInfoMessage,
        goBack = goBack,
    )

    EditAdditionGroupScreen(
        state = viewState.toViewState(),
        onAction = onAction,
        goBack = goBack,
    )
}

@Composable
private fun EditAdditionGroupEffect(
    effects: List<EditAdditionGroupDataState.Event>,
    showInfoMessage: (String, Int) -> Unit,
    goBack: () -> Unit,
    consumeEffects: () -> Unit,
) {
    LaunchedEffect(effects) {
        effects.forEach { effect ->
            when (effect) {
                EditAdditionGroupDataState.Event.GoBackEvent -> {
                    goBack()
                }

                is EditAdditionGroupDataState.Event.ShowUpdateAdditionGroupSuccess -> {
                    showInfoMessage(
                        getString(
                            Res.string.msg_edit_addition_group_updated,
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
private fun EditAdditionGroupScreen(
    state: EditAdditionGroupViewState,
    onAction: (EditAdditionGroupDataState.Action) -> Unit,
    goBack: () -> Unit,
) {
    AdminScaffold(
        backgroundColor = AdminTheme.colors.main.surface,
        title = stringResource(Res.string.title_edit_addition_group),
        backActionClick = {
            onAction(EditAdditionGroupDataState.Action.OnBackClicked)
        },
    ) {
        when (state.state) {
            EditAdditionGroupViewState.State.Loading -> {
                LoadingScreen()
            }

            EditAdditionGroupViewState.State.Error -> {
                ErrorScreen(
                    mainTextId = Res.string.title_common_can_not_load_data,
                    extraTextId = Res.string.msg_common_check_connection_and_retry,
                    onClick = {
                        onAction(EditAdditionGroupDataState.Action.Init)
                    },
                )
            }

            is EditAdditionGroupViewState.State.Success -> {
                EditAdditionGroupSuccessScreen(state = state.state, onAction = onAction)
            }
        }
    }
}

@Composable
private fun EditAdditionGroupSuccessScreen(
    state: EditAdditionGroupViewState.State.Success,
    onAction: (EditAdditionGroupDataState.Action) -> Unit,
) {
    Column {
        AdminTextField(
            modifier = Modifier.padding(16.dp),
            labelText = stringResource(Res.string.hint_edit_addition_group_name),
            value = state.nameField.value,
            onValueChange = { name ->
                onAction(
                    EditAdditionGroupDataState.Action.EditNameAdditionGroup(name),
                )
            },
            errorText = stringResource(state.nameField.errorResId),
            isError = state.nameField.isError,
            enabled = !state.isLoading,
        )
        SwitcherCard(
            elevated = false,
            text = stringResource(Res.string.msg_edit_addition_group_is_visible_menu),
            checked = state.isVisible,
            onCheckChanged = { isVisible ->
                onAction(
                    EditAdditionGroupDataState.Action.OnVisibleMenu(
                        isVisible = isVisible,
                    ),
                )
            },
        )
        AdminHorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
        SwitcherCard(
            hint = stringResource(Res.string.msg_edit_addition_group_single_сhoice_hint),
            elevated = false,
            text = stringResource(Res.string.msg_edit_addition_group_single_addition_menu),
            checked = state.isVisibleSingleChoice,
            onCheckChanged = { isVisibleSingleChoice ->
                onAction(
                    EditAdditionGroupDataState.Action.OnVisibleSingleChoice(
                        isVisibleSingleChoice = isVisibleSingleChoice,
                    ),
                )
            },
        )
        AdminHorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
        Spacer(modifier = Modifier.weight(1f))

        LoadingButton(
            modifier = Modifier.padding(16.dp),
            text = stringResource(Res.string.action_edit_addition_group_save),
            isLoading = state.isLoading,
            onClick = {
                onAction(EditAdditionGroupDataState.Action.OnSaveEditAdditionGroupClick)
            },
        )
    }
}
