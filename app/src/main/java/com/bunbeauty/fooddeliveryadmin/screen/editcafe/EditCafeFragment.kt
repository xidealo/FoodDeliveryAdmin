package com.bunbeauty.fooddeliveryadmin.screen.editcafe

import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bunbeauty.domain.model.cafe.CafeWorkingHours
import com.bunbeauty.domain.model.nonworkingday.FormattedNonWorkingDay
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.compose.AdminScaffold
import com.bunbeauty.fooddeliveryadmin.compose.ScreenPreview
import com.bunbeauty.fooddeliveryadmin.compose.ThemePreview
import com.bunbeauty.fooddeliveryadmin.compose.element.button.FloatingButton
import com.bunbeauty.fooddeliveryadmin.compose.element.card.AdminCard
import com.bunbeauty.fooddeliveryadmin.compose.element.card.AdminCardDefaults
import com.bunbeauty.fooddeliveryadmin.compose.element.card.NavigationTextCard
import com.bunbeauty.fooddeliveryadmin.compose.element.picker.AdminDatePickerDefaults
import com.bunbeauty.fooddeliveryadmin.compose.element.picker.AdminTimePickerDefaults
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme
import com.bunbeauty.fooddeliveryadmin.compose.theme.bold
import com.bunbeauty.fooddeliveryadmin.compose.theme.medium
import com.bunbeauty.fooddeliveryadmin.coreui.SingleStateComposeFragment
import com.bunbeauty.fooddeliveryadmin.main.MessageHost
import com.bunbeauty.presentation.feature.editcafe.EditCafe
import com.bunbeauty.presentation.feature.editcafe.EditCafeViewModel
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime

@AndroidEntryPoint
class EditCafeFragment : SingleStateComposeFragment<EditCafe.ViewDataState, EditCafe.Action, EditCafe.Event>() {

    override val viewModel: EditCafeViewModel by viewModels()

    private val editCafeFragmentArgs: EditCafeFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.handleAction(
            EditCafe.Action.Init(
                cafeUuid = editCafeFragmentArgs.cafeUuid,
                cafeAddress = editCafeFragmentArgs.cafeAddress
            )
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Screen(state: EditCafe.ViewDataState, onAction: (EditCafe.Action) -> Unit) {
        val dateDialogState = rememberMaterialDialogState()
        val deleteConfirmBottomSheetState = rememberModalBottomSheetState()

        AdminScaffold(
            title = state.cafeAddress,
            backActionClick = {
                onAction(EditCafe.Action.BackClick)
            },
            actionButton = {
                if (state.nonWorkingDays == EditCafe.ViewDataState.NonWorkingDays.Empty ||
                    state.nonWorkingDays is EditCafe.ViewDataState.NonWorkingDays.Success
                ) {
                    FloatingButton(
                        iconId = R.drawable.ic_plus,
                        textStringId = R.string.action_edit_cafe_add,
                        onClick = {
                            dateDialogState.show()
                        }
                    )
                }
            },
            actionButtonPosition = FabPosition.End
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                val fromTimeDialogState = rememberMaterialDialogState()
                val toTimeDialogState = rememberMaterialDialogState()

                NavigationTextCard(
                    hintStringId = R.string.hint_edit_cafe_from_time,
                    label = state.cafeWorkingHours.fromTimeText,
                    onClick = {
                        fromTimeDialogState.show()
                    }
                )
                FromTimePickerDialog(
                    dialogState = fromTimeDialogState,
                    initialTime = state.cafeWorkingHours.fromTime,
                    onAction = onAction
                )

                NavigationTextCard(
                    modifier = Modifier.padding(top = 8.dp),
                    hintStringId = R.string.hint_edit_cafe_to_time,
                    label = state.cafeWorkingHours.toTimeText,
                    onClick = {
                        toTimeDialogState.show()
                    }
                )
                ToTimePickerDialog(
                    dialogState = toTimeDialogState,
                    initialTime = state.cafeWorkingHours.toTime,
                    onAction = onAction
                )

                Text(
                    modifier = Modifier.padding(top = 16.dp),
                    text = stringResource(R.string.title_edit_cafe_non_working_days),
                    style = AdminTheme.typography.titleMedium.bold,
                    color = AdminTheme.colors.main.onBackground
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    when (val nonWorkingDays = state.nonWorkingDays) {
                        EditCafe.ViewDataState.NonWorkingDays.Loading -> {
                            CircularProgressIndicator(
                                color = AdminTheme.colors.main.primary
                            )
                        }

                        EditCafe.ViewDataState.NonWorkingDays.Empty -> {
                            Text(
                                text = stringResource(R.string.msg_edit_cafe_empty_non_working_days),
                                style = AdminTheme.typography.bodyMedium,
                                color = AdminTheme.colors.main.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                        }

                        is EditCafe.ViewDataState.NonWorkingDays.Success -> {
                            Column(
                                modifier = Modifier.padding(top = 8.dp),
                                verticalArrangement = spacedBy(8.dp)
                            ) {
                                nonWorkingDays.days.forEach { day ->
                                    NonWorkingDayItem(
                                        day = day,
                                        onAction = onAction,
                                        deleteConfirmBottomSheetState = deleteConfirmBottomSheetState
                                    )
                                }
                            }
                        }
                    }

                    DatePickerDialog(
                        dialogState = dateDialogState,
                        initialDate = state.initialNonWorkingDayDate,
                        yearRange = state.yearRange,
                        minDate = state.minNonWorkingDayDate,
                        onDateSelected = { date ->
                            onAction(EditCafe.Action.AddNonWorkingDay(date))
                        }
                    )
                }
            }
        }
    }

    override fun handleEvent(event: EditCafe.Event) {
        when (event) {
            EditCafe.Event.GoBack -> {
                findNavController().popBackStack()
            }

            EditCafe.Event.ShowFetchDataError -> {
                (activity as? MessageHost)?.showErrorMessage(getString(R.string.error_common_loading_failed))
            }

            EditCafe.Event.ShowUpdateDataError -> {
                (activity as? MessageHost)?.showErrorMessage(getString(R.string.error_common_update_failed))
            }

            EditCafe.Event.ShowSaveDataError -> {
                (activity as? MessageHost)?.showErrorMessage(getString(R.string.error_common_saving_failed))
            }

            EditCafe.Event.ShowDeleteDataError -> {
                (activity as? MessageHost)?.showErrorMessage(getString(R.string.error_common_deleting_failed))
            }

            is EditCafe.Event.ShowConfirmDeletion -> {
                lifecycleScope.launch {
                    ConfirmDeletionBottomSheet.show(parentFragmentManager)?.let { confirmed ->
                        if (confirmed) {
                            viewModel.handleAction(
                                EditCafe.Action.ConfirmDeleteNonWorkingDay(uuid = event.uuid)
                            )
                        }
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun NonWorkingDayItem(
        day: FormattedNonWorkingDay,
        onAction: (EditCafe.Action) -> Unit,
        deleteConfirmBottomSheetState: SheetState
    ) {
        Row {
            AdminCard(
                modifier = Modifier
                    .height(48.dp)
                    .weight(1f),
                clickable = false
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    text = day.date,
                    style = AdminTheme.typography.bodyLarge,
                    color = AdminTheme.colors.main.onSurface
                )
            }
            AdminCard(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .size(48.dp),
                colors = AdminCardDefaults.cardNegativeColors,
                onClick = {
                    onAction(EditCafe.Action.DeleteNonWorkingDay(day.uuid))
                    lifecycleScope.launch {
                        deleteConfirmBottomSheetState.show()
                    }
                }
            ) {
                Icon(
                    modifier = Modifier
                        .padding(12.dp)
                        .size(24.dp),
                    painter = painterResource(R.drawable.ic_delete),
                    tint = AdminTheme.colors.status.onStatus,
                    contentDescription = null
                )
            }
        }
    }

    @Composable
    private fun FromTimePickerDialog(
        dialogState: MaterialDialogState,
        initialTime: LocalTime,
        onAction: (EditCafe.Action) -> Unit
    ) {
        TimePickerDialog(
            dialogState = dialogState,
            titleStringId = R.string.title_edit_cafe_from_time,
            initialTime = initialTime,
            onTimeSelected = { time ->
                onAction(EditCafe.Action.UpdateFromTime(time))
            }
        )
    }

    @Composable
    private fun ToTimePickerDialog(
        dialogState: MaterialDialogState,
        initialTime: LocalTime,
        onAction: (EditCafe.Action) -> Unit
    ) {
        TimePickerDialog(
            dialogState = dialogState,
            titleStringId = R.string.title_edit_cafe_to_time,
            initialTime = initialTime,
            onTimeSelected = { time ->
                onAction(EditCafe.Action.UpdateToTime(time))
            }
        )
    }

    @Composable
    private fun TimePickerDialog(
        dialogState: MaterialDialogState,
        @StringRes titleStringId: Int,
        initialTime: LocalTime,
        onTimeSelected: (LocalTime) -> Unit
    ) {
        var pickedTime by remember {
            mutableStateOf(initialTime)
        }
        MaterialDialog(
            dialogState = dialogState,
            backgroundColor = AdminTheme.colors.main.surface
        ) {
            timepicker(
                title = stringResource(titleStringId),
                colors = AdminTimePickerDefaults.timePickerColors,
                is24HourClock = true,
                initialTime = initialTime,
                waitForPositiveButton = false
            ) { time ->
                pickedTime = time
            }
            Row(modifier = Modifier.padding(bottom = 24.dp, end = 24.dp)) {
                Spacer(modifier = Modifier.weight(1f))
                TextButton(onClick = {
                    dialogState.hide()
                }) {
                    Text(
                        text = stringResource(R.string.action_common_cancel),
                        style = AdminTheme.typography.labelLarge.medium,
                        color = AdminTheme.colors.main.disabled
                    )
                }
                TextButton(
                    modifier = Modifier.padding(start = 8.dp),
                    onClick = {
                        onTimeSelected(pickedTime)
                        dialogState.hide()
                    }
                ) {
                    Text(
                        text = stringResource(R.string.action_common_ok),
                        style = AdminTheme.typography.labelLarge.medium,
                        color = AdminTheme.colors.main.primary
                    )
                }
            }
        }
    }

    @Composable
    private fun DatePickerDialog(
        dialogState: MaterialDialogState,
        initialDate: LocalDate,
        yearRange: IntRange,
        minDate: LocalDate,
        onDateSelected: (LocalDate) -> Unit
    ) {
        var pickedDate by remember {
            mutableStateOf(initialDate)
        }
        MaterialDialog(
            dialogState = dialogState,
            backgroundColor = AdminTheme.colors.main.surface
        ) {
            datepicker(
                initialDate = initialDate,
                title = stringResource(R.string.title_common_select_date),
                colors = AdminDatePickerDefaults.datePickerColors,
                yearRange = yearRange,
                waitForPositiveButton = false,
                allowedDateValidator = { date ->
                    date >= minDate
                }
            ) { date ->
                pickedDate = date
            }
            Row(modifier = Modifier.padding(bottom = 24.dp, end = 24.dp)) {
                Spacer(modifier = Modifier.weight(1f))
                TextButton(
                    onClick = {
                        dialogState.hide()
                    }
                ) {
                    Text(
                        text = stringResource(R.string.action_common_cancel),
                        style = AdminTheme.typography.labelLarge.medium,
                        color = AdminTheme.colors.main.disabled
                    )
                }
                TextButton(
                    modifier = Modifier.padding(start = 8.dp),
                    onClick = {
                        onDateSelected(pickedDate)
                        dialogState.hide()
                    }
                ) {
                    Text(
                        text = stringResource(R.string.action_common_ok),
                        style = AdminTheme.typography.labelLarge.medium,
                        color = AdminTheme.colors.main.primary
                    )
                }
            }
        }
    }

    @ScreenPreview
    @Composable
    private fun SuccessEditCafeScreenPreview() {
        Screen(
            state = EditCafe.ViewDataState(
                cafeUuid = null,
                cafeAddress = "Дубна, ул. Университетская, д. 111",
                cafeWorkingHours = CafeWorkingHours(
                    fromTimeText = "10:30",
                    fromTime = LocalTime.of(10, 30),
                    toTimeText = "20:00",
                    toTime = LocalTime.of(20, 0)
                ),
                nonWorkingDays = EditCafe.ViewDataState.NonWorkingDays.Success(
                    listOf(
                        FormattedNonWorkingDay(
                            uuid = "1",
                            date = "12 август (суббота)",
                            cafeUuid = "cafeUuid"
                        ),
                        FormattedNonWorkingDay(
                            uuid = "2",
                            date = "13 август (воскресенье)",
                            cafeUuid = "cafeUuid"
                        ),
                        FormattedNonWorkingDay(
                            uuid = "3",
                            date = "14 август (понедельник)",
                            cafeUuid = "cafeUuid"
                        )
                    )
                ),
                initialNonWorkingDayDate = LocalDate.of(2023, 10, 29),
                yearRange = IntRange(2023, 2023),
                minNonWorkingDayDate = LocalDate.of(2023, 10, 28)
            ),
            onAction = {}
        )
    }

    @ScreenPreview
    @Composable
    private fun EmptyEditCafeScreenPreview() {
        Screen(
            state = EditCafe.ViewDataState(
                cafeUuid = null,
                cafeAddress = "Дубна, ул. Университетская, д. 111",
                cafeWorkingHours = CafeWorkingHours(
                    fromTimeText = "10:30",
                    fromTime = LocalTime.of(10, 30),
                    toTimeText = "20:00",
                    toTime = LocalTime.of(20, 0)
                ),
                nonWorkingDays = EditCafe.ViewDataState.NonWorkingDays.Empty,
                initialNonWorkingDayDate = LocalDate.of(2023, 10, 29),
                yearRange = IntRange(2023, 2023),
                minNonWorkingDayDate = LocalDate.of(2023, 10, 28)
            ),
            onAction = {}
        )
    }

    @ScreenPreview
    @ThemePreview
    @Composable
    private fun LoadingEditCafeScreenPreview() {
        AdminTheme {
            Screen(
                state = EditCafe.ViewDataState(
                    cafeUuid = null,
                    cafeAddress = "Дубна, ул. Университетская, д. 111",
                    cafeWorkingHours = CafeWorkingHours(
                        fromTimeText = "10:30",
                        fromTime = LocalTime.of(10, 30),
                        toTimeText = "20:00",
                        toTime = LocalTime.of(20, 0)
                    ),
                    nonWorkingDays = EditCafe.ViewDataState.NonWorkingDays.Loading,
                    initialNonWorkingDayDate = LocalDate.of(2023, 10, 29),
                    yearRange = IntRange(2023, 2023),
                    minNonWorkingDayDate = LocalDate.of(2023, 10, 28)
                ),
                onAction = {}
            )
        }
    }
}
