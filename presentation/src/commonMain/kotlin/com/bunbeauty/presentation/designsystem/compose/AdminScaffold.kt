package com.bunbeauty.presentation.designsystem.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.bunbeauty.presentation.designsystem.compose.element.topbar.AdminTopBar
import com.bunbeauty.presentation.designsystem.compose.element.topbar.AdminTopBarAction
import com.bunbeauty.presentation.designsystem.compose.theme.AdminTheme

// TODO ADD PULL TO REFRESH
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScaffold(
    title: String? = null,
    backActionClick: (() -> Unit)? = null,
    topActions: List<AdminTopBarAction> = emptyList(),
    backgroundColor: Color = AdminTheme.colors.main.background,
    actionButton: @Composable () -> Unit = {},
    actionButtonPosition: FabPosition = FabPosition.Center,
    pullRefreshEnabled: Boolean = false,
    refreshing: Boolean = false,
    onRefresh: () -> Unit = {},
    content: (@Composable () -> Unit),
) {
    val appBarState = rememberTopAppBarState()
    val behavior = TopAppBarDefaults.pinnedScrollBehavior(appBarState)
    val scrollBehavior = remember { behavior }

    Scaffold(
        modifier =
            Modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AdminTopBar(
                title = title,
                backActionClick = backActionClick,
                scrollBehavior = scrollBehavior,
                actions = topActions,
            )
        },
        containerColor = AdminTheme.colors.main.background,
        floatingActionButton = actionButton,
        floatingActionButtonPosition = actionButtonPosition,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { padding ->
        Box(
            modifier =
                Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .background(backgroundColor),
        ) {
            content()
        }
    }
}
