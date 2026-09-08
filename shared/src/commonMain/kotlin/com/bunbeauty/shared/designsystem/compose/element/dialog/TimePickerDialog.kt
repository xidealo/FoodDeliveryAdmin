package com.bunbeauty.shared.designsystem.compose.element.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.bunbeauty.shared.designsystem.compose.theme.AdminTheme
import com.bunbeauty.shared.designsystem.compose.theme.medium
import fooddeliveryadmin.shared.generated.resources.Res
import fooddeliveryadmin.shared.generated.resources.action_common_cancel
import fooddeliveryadmin.shared.generated.resources.action_common_ok
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    initialHour: Int,
    initialMinute: Int,
    onDismiss: () -> Unit,
    onConfirm: (hour: Int, minute: Int) -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss,
    ) {
        val timePickerState =
            rememberTimePickerState(
                initialHour = initialHour,
                initialMinute = initialMinute,
                is24Hour = true,
            )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier =
                Modifier.background(
                    color = AdminTheme.colors.main.surface,
                    shape = RoundedCornerShape(size = 16.dp),
                ),
        ) {
            TimePicker(
                modifier = Modifier.padding(top = 16.dp),
                state = timePickerState,
            )

            Row(
                modifier =
                    Modifier
                        .padding(bottom = 24.dp)
                        .padding(horizontal = 16.dp),
            ) {
                Spacer(modifier = Modifier.weight(1f))
                TextButton(
                    onClick = onDismiss,
                ) {
                    Text(
                        text = stringResource(Res.string.action_common_cancel),
                        style = AdminTheme.typography.labelLarge.medium,
                        color = AdminTheme.colors.main.onSurface,
                    )
                }
                TextButton(
                    modifier = Modifier.padding(start = 8.dp),
                    onClick = {
                        onConfirm(
                            timePickerState.hour,
                            timePickerState.minute,
                        )
                    },
                ) {
                    Text(
                        text = stringResource(Res.string.action_common_ok),
                        style = AdminTheme.typography.labelLarge.medium,
                        color = AdminTheme.colors.main.primary,
                    )
                }
            }
        }
    }
}
