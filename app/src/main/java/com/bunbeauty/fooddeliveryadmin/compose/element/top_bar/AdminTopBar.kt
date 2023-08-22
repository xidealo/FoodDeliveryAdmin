package com.bunbeauty.fooddeliveryadmin.compose.element.top_bar

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme
import com.bunbeauty.fooddeliveryadmin.compose.theme.bold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminTopBar(
    title: String?,
    backActionClick: (() -> Unit)? = null,
    scrollBehavior: TopAppBarScrollBehavior,
    actions: List<AdminTopBarAction> = emptyList()
) {
    Box {
        TopAppBar(
            colors = AdminTopBarDefaults.topAppBarColors(),
            title = {
                Text(
                    text = title ?: "",
                    maxLines = 1,
                    style = AdminTheme.typography.titleMedium.bold,
                    overflow = TextOverflow.Ellipsis
                )
            },
            navigationIcon = {
                backActionClick?.let {
                    IconButton(
                        onClick = backActionClick
                    ) {
                        Icon(
                            modifier = Modifier.size(16.dp),
                            painter = painterResource(id = R.drawable.ic_arrow_left),
                            tint = AdminTheme.colors.main.onSurface,
                            contentDescription = null
                        )
                    }
                }
            },
            actions = {
                actions.forEach { action ->
                    AdminAction(action)
                }
            },
            scrollBehavior = scrollBehavior
        )
    }
}

@Composable
private fun AdminAction(action: AdminTopBarAction) {
    IconButton(
        onClick = action.onClick
    ) {
        Icon(
            modifier = Modifier.size(24.dp),
            painter = painterResource(id = action.iconId),
            tint = AdminTheme.colors.main.onSurfaceVariant,
            contentDescription = null
        )
    }
}
