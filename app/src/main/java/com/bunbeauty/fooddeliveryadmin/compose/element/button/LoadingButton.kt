package com.bunbeauty.fooddeliveryadmin.compose.element.button

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.compose.element.button.AdminButtonDefaults.getButtonElevation
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme
import com.bunbeauty.fooddeliveryadmin.compose.theme.medium

@Composable
fun LoadingButton(
    modifier: Modifier = Modifier,
    @StringRes textStringId: Int,
    hasShadow: Boolean = true,
    isLoading: Boolean,
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        colors = AdminButtonDefaults.mainButtonColors,
        shape = AdminButtonDefaults.buttonShape,
        elevation = getButtonElevation(hasShadow),
        enabled = !isLoading
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(AdminTheme.dimensions.smallProgressBarSize),
                color = AdminTheme.colors.main.onDisabled
            )
        } else {
            Text(
                text = stringResource(textStringId),
                style = AdminTheme.typography.labelLarge.medium,
                color = AdminTheme.colors.main.onPrimary
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun LoadingButtonPreview() {
    AdminTheme {
        Box(modifier = Modifier.background(AdminTheme.colors.main.background)) {
            LoadingButton(
                textStringId = R.string.action_order_details_do_not_save,
                isLoading = false
            ) {}
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun LoadingButtonLoadingPreview() {
    AdminTheme {
        Box(modifier = Modifier.background(AdminTheme.colors.main.background)) {
            LoadingButton(
                textStringId = R.string.action_login_enter,
                isLoading = true
            ) {}
        }
    }
}
