package com.bunbeauty.fooddeliveryadmin.compose.screen

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.compose.element.button.MainButton
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme
import com.bunbeauty.fooddeliveryadmin.compose.theme.bold

@Composable
fun ErrorScreen(
    @StringRes mainTextId: Int,
    @StringRes extraTextId: Int? = null,
    onClick: () -> Unit,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(AdminTheme.colors.mainColors.error),
            contentAlignment = Alignment.Center
        ) {
            androidx.compose.material3.Icon(
                modifier = Modifier.size(64.dp),
                painter = painterResource(R.drawable.ic_error),
                tint = AdminTheme.colors.mainColors.onError,
                contentDescription = null
            )
        }
        androidx.compose.material3.Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp)
                .padding(horizontal = AdminTheme.dimensions.mediumSpace),
            text = stringResource(id = mainTextId),
            style = AdminTheme.typography.titleMedium.bold,
            color = AdminTheme.colors.mainColors.onSurface,
            textAlign = TextAlign.Center
        )
        extraTextId?.let {
            androidx.compose.material3.Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = AdminTheme.dimensions.smallSpace)
                    .padding(horizontal = AdminTheme.dimensions.mediumSpace),
                text = stringResource(id = extraTextId),
                style = AdminTheme.typography.bodyLarge,
                color = AdminTheme.colors.mainColors.onSurface,
                textAlign = TextAlign.Center
            )
        }
        Spacer(modifier = Modifier.weight(1f))

        MainButton(
            modifier = Modifier
                .padding(bottom = AdminTheme.dimensions.mediumSpace)
                .padding(horizontal = AdminTheme.dimensions.mediumSpace),
            onClick = onClick,
            textStringId = R.string.action_retry
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun ErrorScreenPreview() {
    AdminTheme {
        ErrorScreen(
            mainTextId = R.string.title_common_can_not_load_data,
            extraTextId = R.string.msg_common_check_connection_and_retry,
            onClick = {}
        )
    }
}
