package com.bunbeauty.shared.designsystem.compose.element.topbar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.bunbeauty.shared.designsystem.compose.theme.AdminTheme
import com.bunbeauty.shared.designsystem.compose.theme.medium
import fooddeliveryadmin.shared.generated.resources.Res
import fooddeliveryadmin.shared.generated.resources.ic_arrow_left
import org.jetbrains.compose.resources.painterResource

private const val MAX_TITLE_LINES = 1

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminTopBar(
    title: String?,
    backActionClick: (() -> Unit)? = null,
    scrollBehavior: TopAppBarScrollBehavior,
    actions: List<AdminTopBarAction> = emptyList(),
) {
    Box {
        TopAppBar(
            colors = AdminTopBarDefaults.topAppBarColors(),
            title = {
                Text(
                    text = title.orEmpty(),
                    maxLines = MAX_TITLE_LINES,
                    style = AdminTheme.typography.titleMedium.medium,
                    overflow = TextOverflow.Ellipsis,
                )
            },
            navigationIcon = {
                backActionClick?.let {
                    IconButton(
                        onClick = backActionClick,
                    ) {
                        Icon(
                            modifier = Modifier.size(16.dp),
                            painter = painterResource(resource = Res.drawable.ic_arrow_left),
                            tint = AdminTheme.colors.main.onSurface,
                            contentDescription = null,
                        )
                    }
                }
            },
            actions = {
                actions.forEach { action ->
                    AdminAction(action)
                }
            },
            scrollBehavior = scrollBehavior,
        )
    }
}

@Composable
private fun AdminAction(action: AdminTopBarAction) {
    IconButton(
        onClick = action.onClick,
    ) {
        Icon(
            modifier = Modifier.size(20.dp),
            painter = painterResource(resource = action.iconId),
            tint = AdminTheme.colors.main.onSurface,
            contentDescription = null,
        )
    }
}
