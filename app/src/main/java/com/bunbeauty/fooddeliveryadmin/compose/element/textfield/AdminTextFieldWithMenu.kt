package com.bunbeauty.fooddeliveryadmin.compose.element.textfield

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bunbeauty.domain.model.Suggestion
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminTextFieldWithMenu(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    value: String = "",
    @StringRes labelStringId: Int,
    @StringRes errorMessageId: Int? = null,
    suggestionsList: List<Suggestion> = emptyList(),
    onSuggestionClick: (suggestion: Suggestion) -> Unit,
    enabled: Boolean = true
) {
    Column(modifier = modifier) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                if (enabled) {
                    onExpandedChange(it)
                }
            }
        ) {
            AdminBaseTextField(
                modifier = Modifier.menuAnchor(),
                value = value,
                labelStringId = labelStringId,
                onValueChange = {},
                isError = errorMessageId != null,
                readOnly = true,
                enabled = false,
                trailingIconId = if (expanded) {
                    R.drawable.ic_collapse_arrow
                } else {
                    R.drawable.ic_expand_arrow
                }
            )

            if (suggestionsList.isNotEmpty()) {
                ExposedDropdownMenu(
                    modifier = Modifier
                        .background(AdminTheme.colors.main.surface)
                        .exposedDropdownSize(),
                    expanded = expanded,
                    onDismissRequest = {
                        onExpandedChange(false)
                    }
                ) {
                    suggestionsList.forEach { suggestion ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = suggestion.value,
                                    color = AdminTheme.colors.main.onSurface,
                                    style = AdminTheme.typography.bodyMedium
                                )
                            },
                            onClick = {
                                onSuggestionClick(suggestion)
                            }
                        )
                    }
                }
            }
        }
        errorMessageId?.let {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 4.dp),
                text = stringResource(errorMessageId),
                style = AdminTheme.typography.bodySmall,
                color = AdminTheme.colors.main.error
            )
        }
    }
}
