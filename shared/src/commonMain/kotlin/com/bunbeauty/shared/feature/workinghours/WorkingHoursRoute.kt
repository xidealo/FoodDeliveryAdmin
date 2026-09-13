package com.bunbeauty.shared.feature.workinghours

import DateTimeUtil
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bunbeauty.domain.model.cafe.CafeWorkingDay
import com.bunbeauty.shared.designsystem.compose.AdminScaffold
import com.bunbeauty.shared.designsystem.compose.bottomBarPadding
import com.bunbeauty.shared.designsystem.compose.element.button.LoadingButton
import com.bunbeauty.shared.designsystem.compose.element.card.NavigationTextCard
import com.bunbeauty.shared.designsystem.compose.element.dialog.TimePickerDialog
import com.bunbeauty.shared.designsystem.compose.screen.ErrorScreen
import com.bunbeauty.shared.designsystem.compose.screen.LoadingScreen
import com.bunbeauty.shared.designsystem.compose.theme.AdminTheme
import com.bunbeauty.shared.designsystem.compose.theme.bold
import fooddeliveryadmin.shared.generated.resources.Res
import fooddeliveryadmin.shared.generated.resources.action_edit_addition_save
import fooddeliveryadmin.shared.generated.resources.error_working_hours_invalid_range
import fooddeliveryadmin.shared.generated.resources.hint_edit_cafe_from_time
import fooddeliveryadmin.shared.generated.resources.hint_edit_cafe_to_time
import fooddeliveryadmin.shared.generated.resources.msg_common_check_connection_and_retry
import fooddeliveryadmin.shared.generated.resources.msg_working_hours_friday
import fooddeliveryadmin.shared.generated.resources.msg_working_hours_monday
import fooddeliveryadmin.shared.generated.resources.msg_working_hours_saturday
import fooddeliveryadmin.shared.generated.resources.msg_working_hours_sunday
import fooddeliveryadmin.shared.generated.resources.msg_working_hours_thursday
import fooddeliveryadmin.shared.generated.resources.msg_working_hours_tuesday
import fooddeliveryadmin.shared.generated.resources.msg_working_hours_updated
import fooddeliveryadmin.shared.generated.resources.msg_working_hours_wednesday
import fooddeliveryadmin.shared.generated.resources.title_common_can_not_load_data
import fooddeliveryadmin.shared.generated.resources.title_working_hours
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun WorkingHoursRouteScreen(
    viewModel: WorkingHoursViewModel = koinViewModel(),
    goBack: () -> Unit,
    showInfoMessage: (String, Dp) -> Unit,
) {
    val dataState by viewModel.state.collectAsStateWithLifecycle()
    val onAction =
        remember {
            { event: WorkingHoursState.Action ->
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
        onAction(WorkingHoursState.Action.Init)
    }

    WorkingHoursEffect(
        effects = effects,
        consumeEffects = consumeEffects,
        goBack = goBack,
        showInfoMessage = showInfoMessage,
    )

    WorkingHoursScreen(
        state = dataState,
        onAction = onAction,
    )
}

@Composable
private fun WorkingHoursEffect(
    effects: List<WorkingHoursState.Event>,
    goBack: () -> Unit,
    showInfoMessage: (String, Dp) -> Unit,
    consumeEffects: () -> Unit,
) {
    LaunchedEffect(effects) {
        effects.forEach { effect ->
            when (effect) {
                WorkingHoursState.Event.GoBackEvent -> {
                    goBack()
                }

                WorkingHoursState.Event.ShowUpdatedEvent -> {
                    goBack()
                    showInfoMessage(getString(Res.string.msg_working_hours_updated), 0.dp)
                }
            }
        }
        consumeEffects()
    }
}

@Composable
private fun WorkingHoursScreen(
    state: WorkingHoursState.DataState,
    onAction: (WorkingHoursState.Action) -> Unit,
) {
    AdminScaffold(
        title = stringResource(Res.string.title_working_hours),
        backActionClick = {
            onAction(WorkingHoursState.Action.OnBackClicked)
        },
        backgroundColor = AdminTheme.colors.main.surface,
        actionButton = {
            if (state.state == WorkingHoursState.DataState.State.SUCCESS) {
                LoadingButton(
                    modifier =
                        Modifier
                            .padding(horizontal = 16.dp)
                            .bottomBarPadding(),
                    text = stringResource(Res.string.action_edit_addition_save),
                    isLoading = state.isLoading,
                    onClick = {
                        onAction(WorkingHoursState.Action.OnSaveClicked)
                    },
                )
            }
        },
    ) {
        when (state.state) {
            WorkingHoursState.DataState.State.LOADING -> {
                LoadingScreen()
            }

            WorkingHoursState.DataState.State.ERROR -> {
                ErrorScreen(
                    mainTextId = Res.string.title_common_can_not_load_data,
                    extraTextId = Res.string.msg_common_check_connection_and_retry,
                    onClick = {
                        onAction(WorkingHoursState.Action.Init)
                    },
                )
            }

            WorkingHoursState.DataState.State.SUCCESS -> {
                WorkingHoursSuccessScreen(
                    state = state,
                    onAction = onAction,
                )
            }
        }
    }
}

@Composable
private fun WorkingHoursSuccessScreen(
    state: WorkingHoursState.DataState,
    onAction: (WorkingHoursState.Action) -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(top = 16.dp)
                .padding(bottom = 88.dp),
    ) {
        state.workingDays.forEach { day ->
            val isInvalid = state.hasInvalidRange && day.fromTime >= day.toTime
            Text(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                text = stringResource(day.dayOfWeek.toDayTitleResId()),
                style = AdminTheme.typography.titleMedium.bold,
                color = AdminTheme.colors.main.onSurface,
            )
            NavigationTextCard(
                elevated = false,
                hasDivider = true,
                labelText = stringResource(Res.string.hint_edit_cafe_from_time),
                valueText = DateTimeUtil.getTimeHHMM(day.fromTime),
                isError = isInvalid,
                errorText = Res.string.error_working_hours_invalid_range,
                onClick = {
                    onAction(WorkingHoursState.Action.OnFromTimeClicked(day.dayOfWeek))
                },
            )
            NavigationTextCard(
                elevated = false,
                labelText = stringResource(Res.string.hint_edit_cafe_to_time),
                valueText = DateTimeUtil.getTimeHHMM(day.toTime),
                isError = isInvalid,
                onClick = {
                    onAction(WorkingHoursState.Action.OnToTimeClicked(day.dayOfWeek))
                },
            )
        }
    }

    state.timePicker?.let { timePicker ->
        TimePickerDialog(
            initialHour = timePicker.hour,
            initialMinute = timePicker.minute,
            onDismiss = {
                onAction(WorkingHoursState.Action.OnTimePickerDismissed)
            },
            onConfirm = { hour, minute ->
                onAction(
                    WorkingHoursState.Action.OnTimePicked(
                        hour = hour,
                        minute = minute,
                    ),
                )
            },
        )
    }
}

private fun Int.toDayTitleResId(): StringResource =
    when (this) {
        1 -> Res.string.msg_working_hours_monday
        2 -> Res.string.msg_working_hours_tuesday
        3 -> Res.string.msg_working_hours_wednesday
        4 -> Res.string.msg_working_hours_thursday
        5 -> Res.string.msg_working_hours_friday
        6 -> Res.string.msg_working_hours_saturday
        7 -> Res.string.msg_working_hours_sunday
        else -> Res.string.msg_working_hours_monday
    }

@Preview
@Composable
private fun WorkingHoursScreenPreview() {
    AdminTheme {
        WorkingHoursScreen(
            state =
                WorkingHoursState.DataState(
                    state = WorkingHoursState.DataState.State.SUCCESS,
                    workingDays =
                        listOf(
                            CafeWorkingDay(
                                dayOfWeek = 1,
                                fromTime = 37_800,
                                toTime = 73_800,
                            ),
                        ),
                    isLoading = false,
                    hasInvalidRange = false,
                    timePicker = null,
                ),
            onAction = {},
        )
    }
}
