package com.bunbeauty.fooddeliveryadmin.screen.editcafe

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.compose.element.bottomsheet.AdminModalBottomSheet
import com.bunbeauty.fooddeliveryadmin.compose.element.button.AdminButtonDefaults
import com.bunbeauty.fooddeliveryadmin.compose.element.button.MainButton
import com.bunbeauty.fooddeliveryadmin.compose.element.button.SecondaryButton
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme

@Composable
fun ConfirmDeletionBottomSheet(
    isShown: Boolean
) {
    AdminModalBottomSheet(
        title = stringResource(R.string.title_edit_cafe_delete_now_working_day),
        isShown = isShown,
        onDismissRequest = {},
    ) {
        Column(verticalArrangement = Arrangement.Absolute.spacedBy(8.dp)) {
            MainButton(
                textStringId = R.string.action_common_delete,
                colors = AdminButtonDefaults.negativeButtonColors,
                onClick = {
                    // todo
                },
            )
            SecondaryButton(
                textStringId = R.string.action_common_cancel,
                onClick = {
                    // todo
                },
            )
        }
    }
}

@Preview
@Composable
private fun ConfirmDeletionBottomSheetPreview() {
    AdminTheme {
        ConfirmDeletionBottomSheet(
            isShown = true
        )
    }
}

